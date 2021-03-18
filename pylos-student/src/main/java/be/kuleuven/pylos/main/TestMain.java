package be.kuleuven.pylos.main;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class TestMain {
    public static void main(String[] args) throws IOException {
       /* List<String> order;

        List<Object> list = new ArrayList<>();
        list.add("A1");
        list.add("A22");
        list.add("E");


        System.out.println();


        for (List permutation : Permutation.getPermutations(list)) {
            order = permutation;
            System.out.println(order);
        }*/


        try {
            // create a writer
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("books.json"));

            // create books list
            List<Book> books = Arrays.asList(
                    new Book("Thinking in Java", "978-0131872486", 1998,
                            new String[]{"Bruce Eckel"}),
                    new Book("Head First Java", "0596009208", 2003,
                            new String[]{"Kathy Sierra", "Bert Bates"})
            );

            // convert books list to JSON and write to books.json
            Jsoner.serialize(books, writer);

            // close the writer
            writer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

class Book implements Jsonable {

    private String title;
    private String isbn;
    private long year;
    private String[] authors;

    public Book() {
    }

    public Book(String title, String isbn, long year, String[] authors) {
        this.title = title;
        this.isbn = isbn;
        this.year = year;
        this.authors = authors;
    }

    // getters and setters, equals(), toString() .... (omitted for brevity)

    @Override
    public String toJson() {
        JsonObject json = new JsonObject();
        json.put("title", this.title);
        json.put("isbn", this.isbn);
        json.put("year", this.year);
        json.put("authors", this.authors);
        return json.toJson();
    }

    @Override
    public void toJson(Writer writable) throws IOException {
        try {
            writable.write(this.toJson());
        } catch (Exception ignored) {
        }
    }
}