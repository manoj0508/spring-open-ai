package com.manoj.spring.ai.app.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HrPolicyDataLoader {

    @Value("classpath:M-Fleet_Employee_HR_Policy.pdf")
    private Resource hrPolicy;

    private VectorStore vectorStore;

    public HrPolicyDataLoader(VectorStore vectorStore){
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadHRPolicyPdfFile(){
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(hrPolicy);
        List<Document> documentList = tikaDocumentReader.get();

        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder().withChunkSize(100).withMaxNumChunks(400).build();
        List<Document> splitDocuments = tokenTextSplitter.split(documentList);

        vectorStore.add(splitDocuments);

    }
}
