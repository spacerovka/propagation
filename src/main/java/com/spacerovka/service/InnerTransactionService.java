package com.spacerovka.service;

import com.spacerovka.BoxDAO;
import com.spacerovka.entity.Box;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Svetotulichka on 15.12.2017.
 */
@Service
public class InnerTransactionService {

    @Autowired
    private BoxDAO boxDAO;

    @Transactional(propagation= Propagation.REQUIRED)
    public void fillWithCandy(Box box) {
        box.setContent("candy");
        boxDAO.save(box);
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void fillWithJellyInNewTransaction(Box box) {
        box.setContent("jelly");
        boxDAO.save(box);
    }

    @Transactional(propagation= Propagation.MANDATORY)
    public void fillWithKitKatInMandatoryTransaction(Box box) {
        box.setContent("kitCat");
        boxDAO.save(box);
    }

    @Transactional(propagation= Propagation.NEVER)
    public void fillWithJellyNever(Box box) {
        box.setContent("jelly");
        boxDAO.save(box);
    }

    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public void independentFill(Box box) {
        box.setContent("kitCat");
        boxDAO.save(box);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    public void fillSupport(Box box) {
        box.setContent("support");
        boxDAO.save(box);
    }

    @Transactional(propagation= Propagation.NESTED)
    public Box setContent(Box box) {
        box.setContent("new content");
        boxDAO.save(box);
        throw new RuntimeException("Rollback");
    }
}
