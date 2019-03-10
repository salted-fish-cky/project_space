package com.cky.spring.transtion;

import java.util.List;

public interface Cashier {
    public void checkout(String username, List<Integer> isbns);
}
