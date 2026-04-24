package com.example.wallet;

public class SmartWallet {

    public enum UserType {
        STANDARD,
        PREMIUM
    }

    private static final double STANDARD_MAX_BALANCE = 5000.0;
    private static final double CASHBACK_THRESHOLD = 100.0;
    private static final double CASHBACK_RATE = 0.01;

    private double balance;
    private final UserType userType;
    private boolean active;

    public SmartWallet(UserType userType) {
        this.userType = userType;
        this.balance = 0.0;
        this.active = true;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }

        double finalAmount = amount;
        if (amount > CASHBACK_THRESHOLD) {
            finalAmount += amount * CASHBACK_RATE;
        }

        if (userType == UserType.STANDARD && (balance + finalAmount) > STANDARD_MAX_BALANCE) {
            return false;
        }

        balance += finalAmount;
        active = true;
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }

        balance -= amount;
        if (balance == 0.0) {
            active = false;
        }

        return true;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public UserType getUserType() {
        return userType;
    }
}

