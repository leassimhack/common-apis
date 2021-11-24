package mx.parrot.commonapis.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import mx.parrot.commonapis.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

public class PDFGenerator {

    private static Logger logger = LoggerFactory.getLogger(PDFGenerator.class);

    public static ByteArrayInputStream customerPDFReport(List<Product> products) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);
            document.open();

            // Add Text to PDF file ->
            Font font = FontFactory.getFont(FontFactory.TIMES_BOLD, 20, -1, BaseColor.BLACK);

            Font fontSubtitle = FontFactory.getFont(FontFactory.HELVETICA, 15, 0, BaseColor.BLACK);

            Font fontHeaderTable = FontFactory.getFont(FontFactory.HELVETICA, 12, -1, BaseColor.BLACK);


            Paragraph para = new Paragraph("*Reporte de ventas de Productos*", font);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);


            Paragraph subtitle = new Paragraph("Reporte de Inventario", fontSubtitle);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            document.add(Chunk.NEWLINE);


            PdfPTable table = new PdfPTable(3);
            // Add PDF Table Header ->
            Stream.of("Producto", "Cantidad", "Importe Total")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                        header.setBackgroundColor(new BaseColor(240, 240, 240));
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        header.setBorderWidth(1);
                        header.setPhrase(new Phrase(headerTitle, headFont));
                        table.addCell(header);
                    });

            for (Product product : products) {

                PdfPCell nameCell = new PdfPCell(new Phrase(product.getName()));
                nameCell.setPaddingLeft(6);
                nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                nameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(nameCell);


                PdfPCell quantity = new PdfPCell(new Phrase("" + product.getQuantity().toString()));
                quantity.setVerticalAlignment(Element.ALIGN_MIDDLE);
                quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
                quantity.setPaddingRight(2);
                table.addCell(quantity);


                PdfPCell amountCell = new PdfPCell(new Phrase("$" + product.getAmount().getValue()));
                amountCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                amountCell.setPaddingRight(4);
                table.addCell(amountCell);


            }
            document.add(table);

            document.close();
        } catch (DocumentException e) {
            logger.error(e.toString());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}