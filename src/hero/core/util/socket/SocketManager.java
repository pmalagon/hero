package hero.core.util.socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import hero.core.problem.Variable;

public class SocketManager {

    private String serverIP;
    private int serverPort;

    public SocketManager() {
    }

    public SocketManager(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public float sendSyncOrder(ArrayList<Variable<Integer>> order){
        if (this.serverIP.equals("") || this.serverPort == 0){
            System.out.println("ERROR: IP AND PORT NEEDED");
            return -1;
        }

	float val = -1;
        try {
            Socket socket = new Socket(serverIP, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeBytes(parseArrayList(order));
            dos.flush();

            String response;
            while ((response = in.readLine()) != null){
                val = Float.valueOf(response);
            }

            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return val;
    }

    private String parseArrayList(ArrayList<Variable<Integer>> arrayList){

        StringBuilder stringListBuilder = new StringBuilder();
        for (Variable item : arrayList) {
            stringListBuilder.append(item.getValue()).append("-");
        }

        String stringList = stringListBuilder.toString();

        if (!stringList.equals("")){
            stringList = stringList.substring(0, stringList.length()-1);
            stringList += ";";
        }


        return stringList;
    }
}
