package ca.autobit.com.serialterminal;

import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.Scanner;

import static java.lang.System.*;

public class Application {
  public static void main(String[] args) throws SerialPortException {
    if(args.length != 5) {
      out.println("Usage: java -jar serialterm.jar [device] [baud] [data bits] [stop bits] [parity bits]");
      return;
    }

    String device = args[0];
    int baudRate = Integer.parseInt(args[1]);
    int dataBits = Integer.parseInt(args[2]);
    int stopBits = Integer.parseInt(args[3]);
    int parityBits = Integer.parseInt(args[4]);

    final SerialPort serialPort = new SerialPort(device);
    serialPort.openPort();
    serialPort.setParams(baudRate, dataBits, stopBits, parityBits);

    new Thread(new InputListener(serialPort)).start();
    new Thread(new OutputListener(serialPort)).start();
  }

  private static class InputListener implements Runnable {
    private final SerialPort serialPort;

    public InputListener(final SerialPort serialPort) {
      this.serialPort = serialPort;
    }

    @Override
    public void run() {
      Scanner scanner = new Scanner(in);

      while(true) {
        String data = scanner.nextLine() + "\r\n";

        try {
          serialPort.writeBytes(data.getBytes());
        }
        catch(SerialPortException e) {
          out.println("Host error: " + e.getMessage());
        }
      }
    }
  }

  private static class OutputListener implements Runnable {
    private final SerialPort serialPort;

    public OutputListener(final SerialPort serialPort) {
      this.serialPort = serialPort;
    }

    @Override
    public void run() {
      while(true) {
        try {
          byte[] data = serialPort.readBytes(1);

          if(data != null) {
            out.print(new String(data).replace("\r", "\r\n"));
          }
        }
        catch(SerialPortException e) {
          out.println("Device error: " + e.getMessage());
        }
      }
    }
  }
}
