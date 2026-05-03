package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format
        // binar, RECORD_SIZE=32 bytes/înreg.)
        // - bytes 0-3: id (int, little-endian via ByteBuffer)
        // - bytes 4-11: suma (double, little-endian via ByteBuffer)
        // - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        // - byte 22: tip (0=CREDIT, 1=DEBIT)
        // - byte 23: status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        // - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        // - READ idx → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        // - UPDATE idx ST → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        // afișează "Updated [idx]: STATUS"
        // - PRINT_ALL → citește și afișează toate înregistrările
        //
        // Format linie output:
        // [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON
        // status=<STATUS>

        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File outputFile = new File(OUTPUT_FILE);

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))) {
            for (int i = 0; i < n; i++) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next().toUpperCase());

                byte[] idBytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array();
                dos.write(idBytes);

                byte[] sumaBytes = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array();
                dos.write(sumaBytes);

                String paddedData = String.format("%-10s", data);
                dos.write(paddedData.getBytes(StandardCharsets.US_ASCII));

                dos.writeByte(tip == TipTranzactie.CREDIT ? 0 : 1);

                dos.writeByte(0);

                dos.write(new byte[8]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (RandomAccessFile raf = new RandomAccessFile(outputFile, "rw")) {
            while (scanner.hasNext()) {
                String command = scanner.next();

                if (command.equals("READ")) {
                    int idx = scanner.nextInt();
                    printRecord(raf, idx);
                } else if (command.equals("UPDATE")) {
                    int idx = scanner.nextInt();
                    String status = scanner.next();
                    updateRecord(raf, idx, status);
                } else if (command.equals("PRINT_ALL")) {
                    long numRecords = raf.length() / RECORD_SIZE;
                    for (int i = 0; i < numRecords; i++) {
                        printRecord(raf, i);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    private static void printRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);

        byte[] record = new byte[RECORD_SIZE];
        raf.readFully(record);

        ByteBuffer buffer = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);

        int id = buffer.getInt(0);
        double suma = buffer.getDouble(4);

        byte[] dataBytes = new byte[10];
        buffer.position(12);
        buffer.get(dataBytes);
        String data = new String(dataBytes, StandardCharsets.US_ASCII).trim();

        byte tipByte = buffer.get(22);
        String tip = (tipByte == 0) ? "CREDIT" : "DEBIT";

        byte statusByte = buffer.get(23);
        String status = (statusByte == 0) ? "PENDING" : (statusByte == 1) ? "PROCESSED" : "REJECTED";

        System.out.printf(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
                idx, id, data, tip, suma, status);
    }

    private static void updateRecord(RandomAccessFile raf, int idx, String statusStr) throws IOException {
        raf.seek((long) idx * RECORD_SIZE + 23);

        byte statusByte = 0;
        if (statusStr.equals("PROCESSED")) {
            statusByte = 1;
        } else if (statusStr.equals("REJECTED")) {
            statusByte = 2;
        }

        raf.write(statusByte);

        System.out.println("Updated [" + idx + "]: " + statusStr);
    }
}
