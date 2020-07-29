package com.google.sps.services.implementations;

import java.sql.Timestamp;

import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.sps.protoc.BlobstoreProtoc.BlobstoreResponse;
import com.google.sps.services.interfaces.BlobstoreService;

public class BlobstoreServiceImpl implements BlobstoreService {
    @Override
    public BlobstoreResponse execute() {
        BlobstoreResponse.Builder blobstoreResponse = BlobstoreResponse.newBuilder();
        blobstoreResponse.setUrl(getUploadUrl());
        blobstoreResponse.setTimestamp(getTimestamp());
        return blobstoreResponse.build();
    }

    public String getUploadUrl() {
        com.google.appengine.api.blobstore.BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        return blobstoreService.createUploadUrl("/my-form-handler");
    }

    public long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }
}
