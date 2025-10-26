package com.android.monu.data.manager

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import com.android.monu.domain.manager.DataManager
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.usecase.finance.BackupData
import com.android.monu.ui.feature.utils.DatabaseCodeMapper
import com.android.monu.ui.feature.utils.DateHelper
import com.android.monu.ui.feature.utils.NumberFormatHelper
import com.android.monu.ui.feature.utils.TransactionType
import com.google.gson.Gson
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.TabSettings
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class DataManagerImpl(
    val context: Context
) : DataManager {
    override fun backupData(backupData: BackupData) {
        val fileName = "monu_backup_${System.currentTimeMillis()}.json"
        val backupDataJson = Gson().toJson(backupData)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/json")
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                ?: throw IOException("Failed insert to MediaStore")

            resolver.openOutputStream(uri)?.use { it.write(backupDataJson.toByteArray()) }
        } else {
            val downloadsDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)
            file.writeText(backupDataJson)
        }
    }

    override fun restoreData(stringUri: String): BackupData {
        val backupDataJson = context.contentResolver.openInputStream(stringUri.toUri())
            ?.bufferedReader().use { it?.readText() }
        val backupData = Gson().fromJson(backupDataJson, BackupData::class.java)
        return backupData
    }

    override fun exportDataToPdf(
        reportName: String,
        username: String,
        startDate: String,
        endDate: String,
        commonTransactions: List<Transaction>,
        transferTransactions: List<Transaction>,
        totalIncome: Long,
        totalExpense: Long
    ) {
        try {
            val fileName = "monu_report_${System.currentTimeMillis()}.json"

            val outputStream: OutputStream? = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                    val contentValues = ContentValues().apply {
                        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                        put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    }
                    val resolver = context.contentResolver
                    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    uri?.let { resolver.openOutputStream(it) }
                }

                else -> {
                    val downloadsDir = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val pdfFile = File(downloadsDir, fileName)
                    FileOutputStream(pdfFile)
                }
            }

            if (outputStream == null) {
                return
            } else {
                createPdfDocument(
                    outputStream = outputStream,
                    reportName = reportName,
                    username = username,
                    startDate = startDate,
                    endDate = endDate,
                    commonTransactions = commonTransactions,
                    transferTransactions = transferTransactions,
                    totalIncome = totalIncome,
                    totalExpense = totalExpense
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createPdfDocument(
        outputStream: OutputStream,
        reportName: String,
        username: String,
        startDate: String,
        endDate: String,
        commonTransactions: List<Transaction>,
        transferTransactions: List<Transaction>,
        totalIncome: Long,
        totalExpense: Long
    ) {
        val document = Document(PageSize.A4)
        PdfWriter.getInstance(document, outputStream)
        document.open()

        val startPeriod = DateHelper.formatDateToReadable(startDate)
        val endPeriod = DateHelper.formatDateToReadable(endDate)

        val title = Paragraph(reportName.uppercase(), customFont(size = 16f, style = Font.BOLD))
            .align(Element.ALIGN_CENTER)
        val username = Paragraph()
            .headerAttributes(title = "Nama pengguna", content = username)
        val generatedAt = Paragraph()
            .headerAttributes(title = "Dibuat pada", content = DateHelper.getTodayDate())
        val period = Paragraph()
            .headerAttributes(title = "Periode laporan", content = "$startPeriod - $endPeriod")

        val commonTransactionTable = createCommonTransactionTable(
            transactions = commonTransactions,
            income = totalIncome,
            expense = totalExpense
        )
        val transferTransactionTable = createTransferTransactionTable(transferTransactions)

        val footer = Paragraph("Dibuat secara otomatis oleh Aplikasi Monu", customFont(style = Font.BOLD))
            .footerAttributes()

        document.add(title)
        document.add(Paragraph("\n"))
        document.add(Paragraph("\n"))
        document.add(username)
        document.add(generatedAt)
        document.add(period)
        document.add(Paragraph("\n"))
        document.add(Paragraph("\n"))
        document.add(commonTransactionTable)

        if (transferTransactions.isNotEmpty()) {
            document.add(Paragraph("\n"))
            document.add(transferTransactionTable)
        }

        document.add(footer)

        document.close()
        outputStream.close()
    }

    private fun createCommonTransactionTable(
        transactions: List<Transaction>,
        income: Long,
        expense: Long
    ): PdfPTable {
        val table = PdfPTable(6)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(1.5f, 1.8f, 1.8f, 1.2f, 1.2f, 1.2f))

        val title = Phrase("TRANSAKSI ARUS KEUANGAN", customFont(size = 10f, style = Font.BOLD))
        val titleCell = PdfPCell(title)
            .defaultPadding()
            .alignCenterMiddle()
            .background(BaseColor.LIGHT_GRAY)
            .colSpan(6)
        table.addCell(titleCell)

        val headers = listOf("TANGGAL", "JUDUL", "KATEGORI", "SUB-KATEGORI", "SUMBER", "NOMINAL")
        headers.forEach { header ->
            val header = Phrase(header, customFont(style = Font.BOLD))
            val headerCell = PdfPCell(header)
                .defaultPadding()
                .alignCenterMiddle()
                .background(BaseColor.LIGHT_GRAY)
            table.addCell(headerCell)
        }

        transactions.forEach { transaction ->
            val categoryString = context
                .getString(DatabaseCodeMapper.toParentCategoryTitle(transaction.parentCategory))
            val subCategoryString = context
                .getString(DatabaseCodeMapper.toChildCategoryTitle(transaction.childCategory))
            val backgroundColor = when (transaction.type) {
                TransactionType.INCOME -> BaseColor(200, 230, 201)
                TransactionType.EXPENSE -> BaseColor(255, 205, 210)
                else -> BaseColor.WHITE
            }

            val date = Phrase(DateHelper.formatDateToReadable(transaction.date), customFont())
            val dateCell = PdfPCell(date)
                .defaultPadding()
                .alignLeftMiddle()
                .background(backgroundColor)

            val title = Phrase(transaction.title, customFont())
            val titleCell = PdfPCell(title)
                .defaultPadding()
                .alignLeftMiddle()
                .background(backgroundColor)

            val category = Phrase(categoryString, customFont())
            val categoryCell = PdfPCell(category)
                .defaultPadding()
                .alignLeftMiddle()
                .background(backgroundColor)

            val subCategory = Phrase(subCategoryString, customFont())
            val subCategoryCell = PdfPCell(subCategory)
                .defaultPadding()
                .alignLeftMiddle()
                .background(backgroundColor)

            val source = Phrase(transaction.sourceName, customFont())
            val sourceCell = PdfPCell(source)
                .defaultPadding()
                .alignCenterMiddle()
                .background(backgroundColor)

            val amount = Phrase(NumberFormatHelper.formatToRupiah(transaction.amount), customFont(style = Font.BOLD))
            val amountCell = PdfPCell(amount)
                .defaultPadding()
                .alignRightMiddle()
                .background(backgroundColor)

            table.addCell(dateCell)
            table.addCell(titleCell)
            table.addCell(categoryCell)
            table.addCell(subCategoryCell)
            table.addCell(sourceCell)
            table.addCell(amountCell)
        }

        val totalIncomeText = Phrase("TOTAL PEMASUKAN", customFont(style = Font.BOLD))
        val totalIncomeTextCell = PdfPCell(totalIncomeText)
            .defaultPadding()
            .alignCenterMiddle()
            .background(BaseColor.LIGHT_GRAY)
            .colSpan(5)

        val totalIncome = Phrase(NumberFormatHelper.formatToRupiah(income), customFont(style = Font.BOLD))
        val totalIncomeCell = PdfPCell(totalIncome)
            .defaultPadding()
            .alignRightMiddle()
            .background(BaseColor(200, 230, 201))

        val totalExpenseText = Phrase("TOTAL PENGELUARAN", customFont(style = Font.BOLD))
        val totalExpenseTextCell = PdfPCell(totalExpenseText)
            .defaultPadding()
            .alignCenterMiddle()
            .background(BaseColor.LIGHT_GRAY)
            .colSpan(5)

        val totalExpense = Phrase(NumberFormatHelper.formatToRupiah(expense), customFont(style = Font.BOLD))
        val totalExpenseCell = PdfPCell(totalExpense)
            .defaultPadding()
            .alignRightMiddle()
            .background(BaseColor(255, 205, 210))

        val totalBalanceText = Phrase("TOTAL SALDO AKHIR", customFont(style = Font.BOLD))
        val totalBalanceTextCell = PdfPCell(totalBalanceText)
            .defaultPadding()
            .alignCenterMiddle()
            .background(BaseColor.LIGHT_GRAY)
            .colSpan(5)

        val totalBalance = Phrase(NumberFormatHelper.formatToRupiah(income - expense), customFont(style = Font.BOLD))
        val totalBalanceCell = PdfPCell(totalBalance)
            .defaultPadding()
            .alignRightMiddle()
            .background(BaseColor.WHITE)

        table.addCell(totalIncomeTextCell)
        table.addCell(totalIncomeCell)
        table.addCell(totalExpenseTextCell)
        table.addCell(totalExpenseCell)
        table.addCell(totalBalanceTextCell)
        table.addCell(totalBalanceCell)

        return table
    }

    private fun createTransferTransactionTable(transactions: List<Transaction>): PdfPTable {
        val table = PdfPTable(5)
        table.widthPercentage = 100f

        val title = Phrase("TRANSAKSI TRANSFER", customFont(size = 10f, style = Font.BOLD))
        val titleCell = PdfPCell(title)
            .defaultPadding()
            .alignCenterMiddle()
            .background(BaseColor.LIGHT_GRAY)
            .colSpan(5)
        table.addCell(titleCell)

        val headers = listOf("TANGGAL", "KATEGORI", "SUMBER", "TUJUAN", "NOMINAL")
        headers.forEach { header ->
            val header = Phrase(header, customFont(style = Font.BOLD))
            val headerCell = PdfPCell(header)
                .defaultPadding()
                .alignCenterMiddle()
                .background(BaseColor.LIGHT_GRAY)
            table.addCell(headerCell)
        }

        transactions.forEach { transaction ->
            val categoryString = context
                .getString(DatabaseCodeMapper.toChildCategoryTitle(transaction.childCategory))
            val backgroundColor = BaseColor(187, 222, 251)

            val date = Phrase(DateHelper.formatDateToReadable(transaction.date), customFont())
            val dateCell = PdfPCell(date)
                .defaultPadding()
                .alignLeftMiddle()
                .background(backgroundColor)

            val category = Phrase(categoryString, customFont())
            val categoryCell = PdfPCell(category)
                .defaultPadding()
                .alignLeftMiddle()
                .background(backgroundColor)

            val source = Phrase(transaction.sourceName, customFont())
            val sourceCell = PdfPCell(source)
                .defaultPadding()
                .alignCenterMiddle()
                .background(backgroundColor)

            val destination = Phrase(transaction.sourceName, customFont())
            val destinationCell = PdfPCell(destination)
                .defaultPadding()
                .alignCenterMiddle()
                .background(backgroundColor)

            val amount = Phrase(NumberFormatHelper.formatToRupiah(transaction.amount), customFont(style = Font.BOLD))
            val amountCell = PdfPCell(amount)
                .defaultPadding()
                .alignRightMiddle()
                .background(backgroundColor)

            table.addCell(dateCell)
            table.addCell(categoryCell)
            table.addCell(sourceCell)
            table.addCell(destinationCell)
            table.addCell(amountCell)
        }

        return table
    }

    private fun Paragraph.align(alignment: Int): Paragraph {
        this.alignment = alignment
        return this
    }

    private fun Paragraph.headerAttributes(
        title: String,
        content: String
    ): Paragraph {
        this.font = Font(Font.FontFamily.HELVETICA, 10f)
        this.tabSettings = TabSettings(120f)
        this.add(Chunk(title))
        this.add(Chunk.TABBING)
        this.add(Chunk(": $content"))
        return this
    }

    private fun Paragraph.footerAttributes(): Paragraph {
        this.alignment = Element.ALIGN_RIGHT
        this.spacingBefore = 20f
        return this
    }

    private fun customFont(
        family: Font.FontFamily = Font.FontFamily.HELVETICA,
        size: Float = 8f,
        style: Int = Font.NORMAL,
        color: BaseColor = BaseColor.BLACK
    ): Font {
        return Font(family, size, style, color)
    }

    private fun PdfPCell.defaultPadding(): PdfPCell {
        this.paddingLeft = 4f
        this.paddingRight = 4f
        this.paddingTop = 4f
        this.paddingBottom = 6f
        return this
    }

    private fun PdfPCell.alignCenterMiddle(): PdfPCell {
        this.horizontalAlignment = Element.ALIGN_CENTER
        this.verticalAlignment = Element.ALIGN_MIDDLE
        return this
    }

    private fun PdfPCell.alignLeftMiddle(): PdfPCell {
        this.horizontalAlignment = Element.ALIGN_LEFT
        this.verticalAlignment = Element.ALIGN_MIDDLE
        return this
    }

    private fun PdfPCell.alignRightMiddle(): PdfPCell {
        this.horizontalAlignment = Element.ALIGN_RIGHT
        this.verticalAlignment = Element.ALIGN_MIDDLE
        return this
    }

    private fun PdfPCell.background(color: BaseColor): PdfPCell {
        this.backgroundColor = color
        return this
    }

    private fun PdfPCell.colSpan(count: Int): PdfPCell {
        this.colspan = count
        return this
    }
}