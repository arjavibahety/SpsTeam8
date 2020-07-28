package com.google.sps.services;

import java.io.IOException;

import com.google.sps.util.HtmlParser;

public class LandingServiceImpl implements LandingService {
    @Override
    public String getParsedHtmlString() throws IOException {
        return HtmlParser.parseHtmlFromFile("landing.html");
    }
}
