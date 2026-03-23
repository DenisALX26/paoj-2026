package com.pao.laboratory05.audit;

import java.time.LocalDateTime;
import java.util.Arrays;

import com.pao.laboratory05.angajati.Angajat;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];
    private AuditEntry[] auditLog = new AuditEntry[0];

    private AngajatService() {}

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    private void logAction(String action, String target) {
        AuditEntry entry = new AuditEntry(action, target, LocalDateTime.now().toString());
        AuditEntry[] newLog = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0, newLog, 0, auditLog.length);
        newLog[auditLog.length] = entry;
        auditLog = newLog;
    }

    public void addAngajat(Angajat a) {
        Angajat[] newAngajati = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, newAngajati, 0, angajati.length);
        newAngajati[newAngajati.length - 1] = a;
        angajati = newAngajati;
        System.out.println("Angajat adaugat: " + a.getNume());
        logAction("ADD", a.getNume());
    }

    public void findByDepartment(String nume) {
        logAction("FIND_BY_DEPT", nume);
        System.out.println("Angajati din " + nume);
        boolean found = false;
        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(nume)) {
                System.out.println(a);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Nu avem angajati");
        }
    }

    public void listBySalary() {
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);
        for (Angajat a : copy) {
            System.out.println(a);
        }
    }

    public void printAuditLog() {
        System.out.println("Audit log:");
        for (AuditEntry entry : auditLog) {
            System.out.println(entry);
        }
    }
}
