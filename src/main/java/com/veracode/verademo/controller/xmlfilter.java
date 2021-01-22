package com.veracode.verademo.controller;

import org.apache.commons.fileupload.MultipartStream;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class xmlfilter {

  public static void main(String[] args) {
    String candidate = args[0];
    String hashed = BCrypt.hashpw(candidate, BCrypt.gensalt(12));

    BCrypt.checkpw(candidate, hashed);

    filterXMLSignature();
  }

  private static void filterXMLSignature() {
    byte[] bytes = new byte[256];

    new MultipartStream(new ByteArrayInputStream(bytes), bytes);

    new XMLSignatureInput(bytes).addNodeFilter(null);
  }
}
