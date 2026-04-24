package com.example.wallet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmartWalletTest {

    private static final double DELTA = 0.0001;

    private SmartWallet standardWallet;

    @BeforeEach
    void setUp() {
        standardWallet = new SmartWallet(SmartWallet.UserType.STANDARD);
    }

    @Test
    void depositValidoMenorOIgualA100SinCashback() {
        boolean result = standardWallet.deposit(100.0);

        assertTrue(result);
        assertEquals(100.0, standardWallet.getBalance(), DELTA);
    }

    @Test
    void depositMayorA100AplicaCashbackDel1Porciento() {
        boolean result = standardWallet.deposit(200.0);

        assertTrue(result);
        assertEquals(202.0, standardWallet.getBalance(), DELTA);
    }

    @Test
    void depositNoPermiteMontoCeroONegativo() {
        assertFalse(standardWallet.deposit(0.0));
        assertFalse(standardWallet.deposit(-10.0));
        assertEquals(0.0, standardWallet.getBalance(), DELTA);
    }

    @Test
    void standardNoPuedeSuperarLimiteDe5000ConCashback() {
        assertTrue(standardWallet.deposit(4900.0)); // 4900 + 49 = 4949

        boolean secondDeposit = standardWallet.deposit(100.0); // quedaria 5049

        assertFalse(secondDeposit);
        assertEquals(4949.0, standardWallet.getBalance(), DELTA);
    }

    @Test
    void standardPermiteLlegarExactamenteALimite5000() {
        assertTrue(standardWallet.deposit(4900.0)); // saldo 4949

        boolean secondDeposit = standardWallet.deposit(50.0); // no cashback, saldo 4999
        boolean thirdDeposit = standardWallet.deposit(1.0); // saldo 5000

        assertTrue(secondDeposit);
        assertTrue(thirdDeposit);
        assertEquals(5000.0, standardWallet.getBalance(), DELTA);
    }

    @Test
    void retiroValidoDescuentaDelSaldo() {
        standardWallet.deposit(100.0);

        boolean result = standardWallet.withdraw(40.0);

        assertTrue(result);
        assertEquals(60.0, standardWallet.getBalance(), DELTA);
        assertTrue(standardWallet.isActive());
    }

    @Test
    void retiroNoPermiteMontoCeroONegativo() {
        standardWallet.deposit(100.0);

        assertFalse(standardWallet.withdraw(0.0));
        assertFalse(standardWallet.withdraw(-1.0));
        assertEquals(100.0, standardWallet.getBalance(), DELTA);
    }

    @Test
    void retiroNoPermiteSaldoInsuficiente() {
        standardWallet.deposit(50.0);

        boolean result = standardWallet.withdraw(70.0);

        assertFalse(result);
        assertEquals(50.0, standardWallet.getBalance(), DELTA);
    }

    @Test
    void cuentaSeInactivaCuandoSaldoQuedaEnCero() {
        standardWallet.deposit(80.0);

        boolean result = standardWallet.withdraw(80.0);

        assertTrue(result);
        assertEquals(0.0, standardWallet.getBalance(), DELTA);
        assertFalse(standardWallet.isActive());
    }

    @Test
    void premiumNoTieneLimiteDe5000() {
        SmartWallet premiumWallet = new SmartWallet(SmartWallet.UserType.PREMIUM);

        boolean result = premiumWallet.deposit(6000.0); // 6000 + 60 cashback

        assertTrue(result);
        assertEquals(6060.0, premiumWallet.getBalance(), DELTA);
    }
}

