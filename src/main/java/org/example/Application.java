package org.example;

import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.blocks.MethodCall;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.Util;

import java.lang.reflect.Method;
import java.util.Random;

public class Application extends ReceiverAdapter {
    private volatile View view;

    public static void main(String[] args) throws Exception {
        new Application().start();
    }

    private void start() throws Exception {
        Method method = Application.class.getMethod("getNumber", String.class);
        JChannel channel = new JChannel("jgroups.xml");
        RpcDispatcher dispatcher = new RpcDispatcher(channel, this);
        dispatcher.setMembershipListener(this);
        channel.connect("test-cluster");
        Runtime.getRuntime().addShutdownHook(new Thread(channel::close));

        while (true) {
            dispatcher.callRemoteMethods(null, new MethodCall(method, channel.getAddress().toString()), RequestOptions.SYNC());
            Util.sleep(5000);
        }
    }

    public int getNumber(String caller) {
        //System.out.println("Member " + caller + " called getNumber()");
        System.out.println("# of members in a cluster: " + view.getMembers().size());
        return new Random().nextInt();
    }

    @Override
    public void viewAccepted(View view) {
        this.view = view;
        System.out.println(view);
    }
}
