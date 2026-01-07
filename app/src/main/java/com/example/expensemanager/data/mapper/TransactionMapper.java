package com.example.expensemanager.data.mapper;

import com.example.expensemanager.domain.entity.TransactionEntity;
import com.example.expensemanager.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionMapper {

    public static TransactionEntity toEntity(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        TransactionEntity entity = new TransactionEntity();
        entity.setId(transaction.getId());
        entity.setType(transaction.getType());
        entity.setCategory(transaction.getCategory());
        entity.setAccount(transaction.getAccount());
        entity.setNote(transaction.getNote());
        entity.setDate(transaction.getDate());
        entity.setAmount(transaction.getAmount());
        return entity;
    }

    public static Transaction toModel(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setId((int) entity.getId());
        transaction.setType(entity.getType());
        transaction.setCategory(entity.getCategory());
        transaction.setAccount(entity.getAccount());
        transaction.setNote(entity.getNote());
        transaction.setDate(entity.getDate());
        transaction.setAmount(entity.getAmount());
        return transaction;
    }

    public static ArrayList<Transaction> toModelList(List<TransactionEntity> entities) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        if (entities != null) {
            for (TransactionEntity entity : entities) {
                transactions.add(toModel(entity));
            }
        }
        return transactions;
    }

    public static ArrayList<TransactionEntity> toEntityList(List<Transaction> transactions) {
        ArrayList<TransactionEntity> entities = new ArrayList<>();
        if (transactions != null) {
            for (Transaction transaction : transactions) {
                entities.add(toEntity(transaction));
            }
        }
        return entities;
    }
}

