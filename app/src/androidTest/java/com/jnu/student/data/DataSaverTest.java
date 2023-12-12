package com.jnu.student.data;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.jnu.student.Book;
import com.jnu.student.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class DataSaverTest {
    DataSaver dataSaverBackup;
    ArrayList<Book> booksBackup;
    @Before
    public void setUp() throws Exception {
        dataSaverBackup = new DataSaver();
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        booksBackup = dataSaverBackup.Load(targetContext);
    }

    @After
    public void tearDown() throws Exception {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        dataSaverBackup.Save(targetContext,booksBackup);
    }

    @Test
    public void saveAndLoadShopItems() {
        DataSaver dataSaver=new DataSaver();
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Book> books=new ArrayList<>();
        Book book=new Book("测试",R.drawable.book_2);
        books.add(book);
        book=new Book("正常",R.drawable.book_1);
        books.add(book);
        dataSaver.Save(targetContext,books);

        DataSaver dataLoader=new DataSaver();
        ArrayList<Book> shopItemsRead=dataLoader.Load(targetContext);
        assertNotSame(books,shopItemsRead);
        assertEquals(books.size(),shopItemsRead.size());
        for(int index=0;index<books.size();++index)
        {
            assertNotSame(books.get(index),shopItemsRead.get(index));
            assertEquals(books.get(index).getTitle(),shopItemsRead.get(index).getTitle());
            assertEquals(books.get(index).getCoverResource(),shopItemsRead.get(index).getCoverResource());
        }

    }
}