package com.generalidevelopment.hit2013;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

import com.generalidevelopment.hit2013.domain.Item;

@Component
@Configurable
@RooDataOnDemand(entity = Item.class)
public class ItemDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Item> data;

	public Item getNewTransientItem(int index) {
        Item obj = new Item();
        setName(obj, index);
        setPrice(obj, index);
        return obj;
    }

	public void setName(Item obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }

	public void setPrice(Item obj, int index) {
        BigDecimal price = BigDecimal.valueOf(index);
        obj.setPrice(price);
    }

	public Item getSpecificItem(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Item obj = data.get(index);
        Long id = obj.getId();
        return Item.findItem(id);
    }

	public Item getRandomItem() {
        init();
        Item obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Item.findItem(id);
    }

	public boolean modifyItem(Item obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = Item.findItemEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Item' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Item>();
        for (int i = 0; i < 10; i++) {
            Item obj = getNewTransientItem(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
}
