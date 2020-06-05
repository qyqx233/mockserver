package me.zh.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ReadXMLByDOMWithXpath {
    private static DocumentBuilderFactory dBuilderFactory = null;
    private static DocumentBuilder dBuilder = null;
    private static XPathFactory xPathFactory = null;
    private static XPath xPath = null;

    static {
        try {
            /**
             * 要读入一个XML文档，首先要有一个DocumentBuilder对象 可以从DocumentBuilderFactory中得到这个对象
             */
            dBuilderFactory = DocumentBuilderFactory.newInstance();
            //设置验证
//            dBuilderFactory.setValidating(true);
            //忽略空白字符节点
            dBuilderFactory.setIgnoringElementContentWhitespace(true);
            dBuilder = dBuilderFactory.newDocumentBuilder();
            xPathFactory = XPathFactory.newInstance();
            xPath = xPathFactory.newXPath();
            dBuilder.setErrorHandler(new ErrorHandler() {

                public void warning(SAXParseException exception) throws SAXException {
                    throw exception;

                }

                public void fatalError(SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    throw exception;

                }

                public void error(SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    throw exception;
                }
            });
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //获得所有的author节点并输出
    public static void showAuthorByXPath(String fileName) throws SAXException, IOException, XPathExpressionException {
        Document document = dBuilder.parse(fileName);
        NodeList nodeList = (NodeList) xPath.evaluate("/bookstore/book/author", document, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element author = (Element) nodeList.item(i);
            System.out.println(author.getTextContent().trim());
        }
    }

    //解析成book对象
    public static List<Book> listBooksWithXpath(String fileName)
            throws XPathExpressionException, SAXException, IOException {
        List<Book> books = new ArrayList<Book>();
        // 可通过DocumentBuilder对象的parse()方法读入整个文档
        Document document = dBuilder.parse(fileName);
        // 获得所有book节点
        NodeList nodeList = (NodeList) xPath.evaluate("/bookstore/book", document, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (false) {
                Element bookElement = (Element) nodeList.item(i);
                // 获得该book节点下的所有属性节点
                NodeList bookAttribute = bookElement.getChildNodes();
                // 用来存储第i个节点的内容
                List<String> bookContent = new ArrayList<String>();
                Book book = new Book();
                book.setId(Integer.parseInt(bookElement.getAttribute("id").replace("book", "").trim()));
                for (int j = 0; j < bookAttribute.getLength(); j++) {
                    Element atturbute = (Element) bookAttribute.item(j);
                    bookContent.add(atturbute.getTextContent().trim());
                }
                book.setName(bookContent.get(0));
                book.setAuthor(bookContent.get(1));
                book.setYear(Integer.parseInt(bookContent.get(2)));
                book.setPrice(Integer.parseInt(bookContent.get(3)));
                books.add(book);
            }
        }
        return books;
    }

    public static void main(String args[]) {
        try {
            try(InputStream inputStream = ReadXMLByDOMWithXpath.class.getClassLoader().getResource("static/id.html").openStream()) {
//                inputStream.read()
//                byte[] aa = inputStream.readAllBytes();
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
//        URL ins = ;
        String fileName = "./src/main/java/me/zh/xml/bookstore.xml";
        try {
            List<Book> books = ReadXMLByDOMWithXpath.listBooksWithXpath(fileName);
            for (Book book : books) {
                System.out.println(book);
            }
            showAuthorByXPath(fileName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}