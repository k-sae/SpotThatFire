package com.example.kareem.spotthatfire.Connection;

public class Request {
    private String keyword;
    private transient static final String separator = "#@###";
    private String content;

    public Request(String keyword, String content) {
        this.keyword = keyword;
        this.content = content;
    }

    public Request() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public static Request fromString(String s){
        String[] reqs =  s.split(separator);
        return new Request(reqs[0], reqs[1]);
    }

    @Override
    public String toString() {
        return keyword + separator + content + "\n";
    }
}
