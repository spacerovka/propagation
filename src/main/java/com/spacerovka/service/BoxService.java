package com.spacerovka.service;

import com.spacerovka.BoxDAO;
import com.spacerovka.entity.Box;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Svetotulichka on 15.12.2017.
 */
@Service
public class BoxService {

    @Autowired
    private BoxDAO boxDAO;

    @Autowired
    private InnerTransactionService innerTransactionService;

    @Transactional
    public Box fill(Box box) {
        innerTransactionService.fillWithCandy(box);
        throw new RuntimeException("Throw to rollback");
    }

    @Transactional
    public Box noRollBack(Box box) {
        innerTransactionService.fillWithJellyInNewTransaction(box);
        throw new RuntimeException("Throw to rollback");
    }

    public void fillWithKitKat(Box box) {
        innerTransactionService.fillWithKitKatInMandatoryTransaction(box);
    }

    @Transactional
    public void fillWithJellyNever(Box box) {
        innerTransactionService.fillWithJellyNever(box);
    }

    @Transactional
    public void fillNoTransaction(Box box) {
        innerTransactionService.independentFill(box);
        throw new RuntimeException("Throw to rollback");
    }

    @Transactional
    public void support(Box box) {
        innerTransactionService.fillSupport(box);
        throw new RuntimeException("Throw to rollback");

    }

    @Transactional
    public void changeNameAndContent(Box box) {
        try{
            innerTransactionService.setContent(box);
        }catch (RuntimeException e){

        }
        box.setName("New name");
        boxDAO.save(box);
    }
}
