package com.b1Banking.ZenBanking.Services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.b1Banking.ZenBanking.Entity.PdfEntity;
import com.b1Banking.ZenBanking.Repo.PdfRepo;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Service
public class MiniPdfService {

    @Autowired
    private PdfRepo pdfRepo;

    public byte[] generateMiniStatement(Long accountNo) throws Exception {

    	List<PdfEntity> list = pdfRepo.findTop10ByAccountNoOrderByTxnDateDesc(accountNo);

    	PDDocument document = new PDDocument();
    	PDPage page = new PDPage();
    	document.addPage(page);
    	PDPageContentStream contentStream = new PDPageContentStream(document, page);

    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    	contentStream.beginText();
    	contentStream.setFont(PDType1Font.HELVETICA, 10);
    	contentStream.setLeading(14f);
    	contentStream.newLineAtOffset(50, 750);

    	
    	contentStream.showText("DEVBANK OF INDIA");
    	contentStream.newLine();
    	contentStream.newLine();

    
    	contentStream.showText("Bank Name : DevBank of India");
    	contentStream.newLine();
    	contentStream.showText("Branch     : Hyderabad Main Branch");
    	contentStream.newLine();
    	contentStream.showText("IFSC       :DEVB000123");
    	contentStream.newLine();
    	contentStream.showText("Date       : " + sdf.format(new Date()));
    	contentStream.newLine();
    	contentStream.showText("-------------------------------------------------------------------------------------------");
    	contentStream.newLine();

    	if (!list.isEmpty()) {
    	    PdfEntity first = list.get(0);
    	    contentStream.showText("User Name : "+first.getAccountHolder());
        	contentStream.newLine();
    	    contentStream.showText("Account Number : **0000" + first.getAccountNo());
    	    contentStream.newLine();

    	    contentStream.showText("---------------------------------------------------------------------------------------");
    	    contentStream.newLine();
    	    contentStream.newLine();

    	    // ===== TRANSACTIONS =====
    	    contentStream.showText("  Date                              Type                               Amount                              Balance         ");
    	    contentStream.newLine();
    	    contentStream.showText("----------------------------------------------------------------------------------------");
    	    contentStream.newLine();

    	    for (PdfEntity txn : list) {
    	    contentStream.showText( sdf.format(txn.getTxnDate()) + "      |  "    + txn.getTxnType() + "     |  " + txn.getAmount()+"      |   " + txn.getBalanceAfterTxn()  );
    	        contentStream.newLine();
    	    }
    	} else {
    	    contentStream.showText("No transactions found for this account.");
    	    contentStream.newLine();
    	}

    	contentStream.newLine();
    	contentStream.showText("------------------------------------------------------------------------------------------");
    	contentStream.newLine();

    	
    	contentStream.showText("This is a system generated mail don't reply.");
    	contentStream.newLine();
    	contentStream.newLine();
    	contentStream.showText("------------------------------------------Thank you----------------------------------------");

    	contentStream.endText();
    	contentStream.close();

    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	document.save(baos);
    	document.close();

    	return baos.toByteArray();
    }
}
