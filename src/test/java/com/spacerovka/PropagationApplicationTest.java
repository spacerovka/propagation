package com.spacerovka;

import com.spacerovka.entity.Box;
import com.spacerovka.service.BoxService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.IllegalTransactionStateException;

import javax.persistence.EntityManagerFactory;
import javax.transaction.SystemException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PropagationApplicationTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private BoxDAO boxDAO;

    @Autowired
    private BoxService boxService;

    @Test
    public void contextLoads() {
    }

    @Before
    public void init() {
        Box chocolate = new Box("Pink box", "chocolate");
        chocolate.setId(1L);
        boxDAO.save(chocolate);
    }

    @Test
    public void requiredTest() throws SystemException {
        //Code will always run in a transaction. Create a new transaction or reuse one if available
        Box box = boxDAO.getOne(1L);
        try {
            boxService.fill(box);
        } catch (RuntimeException e) {
            assertEquals("Throw to rollback", e.getMessage());
        }
        //all was in already created transaction and data rollback, no changes in DB
        assertEquals("chocolate", boxDAO.getOne(1L).getContent());
    }

    @Test
    public void requiredNewTest() throws SystemException {
        //Code will always run in a new transaction. Suspend current transaction if one exist.
        Box box = boxDAO.getOne(1L);
        try {
            boxService.noRollBack(box);
        } catch (RuntimeException e) {
            assertEquals("Throw to rollback", e.getMessage());
        }
        //all was in new transaction. Rolback did nothing. Data in DB is already comited
        assertEquals("jelly", boxDAO.getOne(1L).getContent());
    }

    @Test(expected = IllegalTransactionStateException.class)
    public void mandatoryTest() {
        //existing opened transaction must already exist. If not an exception will be thrown
        Box box = boxDAO.getOne(1L);
        boxService.fillWithKitKat(box);
    }

    @Test(expected = IllegalTransactionStateException.class)
    public void neverTest() {
        //existing opened transaction must not already exist. If a transaction exists an exception will be thrown
        Box box = boxDAO.getOne(1L);
        boxService.fillWithJellyNever(box);
    }


    @Test
    public void testUnsopported() {
        //NOT_SUPPORTED behavior will execute outside of the scope of any transaction. If an opened transaction already exists it will be paused
        Box box = boxDAO.getOne(1L);
        try {
            boxService.fillNoTransaction(box);
        } catch (RuntimeException e) {
            assertEquals("Throw to rollback", e.getMessage());
        }
        //all was outside transaction. Rolback did nothing. Data in DB is already comited
        assertEquals("kitCat", boxDAO.getOne(1L).getContent());
    }

    @Test
    public void testSupports() {
        //execute in the scope of a transaction if an opened transaction already exists
        Box box = boxDAO.getOne(1L);
        try {
            boxService.support(box);
        } catch (RuntimeException e) {
            assertEquals("Throw to rollback", e.getMessage());
        }
        //transaction rollback, no changes in data
        assertEquals("chocolate", boxDAO.getOne(1L).getContent());
    }

    @Test
    public void testNested() {
        //inner transactions may also rollback independently of outer transactions
        Box box = boxDAO.getOne(1L);
        try {
            boxService.changeNameAndContent(box);
        } catch (RuntimeException e) {
            assertEquals("Throw to rollback", e.getMessage());
        }
        //transaction rollback, no changes in content data
        //outer transaction uis ok, name changed
        assertEquals("chocolate", boxDAO.getOne(1L).getContent());
        assertEquals("New name", box.getName());
    }
}
