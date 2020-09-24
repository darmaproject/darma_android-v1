package com.darma.wallet.utils;

import android.content.Context;

import com.darma.wallet.BuildConfig;
import com.darma.wallet.R;
import com.darma.wallet.db.NodeDB;
import com.darma.wallet.db.WalletDataBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darma Project on 2019/10/25.
 */
public class NodeUtils {


    static{
        NodeDB node=new NodeDB();

        node.setId(-100);
        node.setDefault(true);


        NODE_AUTO=node;
    }

    public static final  NodeDB NODE_AUTO;


    public static final String[] TEST_NODES={""};

    public static final String[] DEFAULT_NODES={"app0.darmacash.com:33804","app1.darmacash.com:33804","app2.darmacash.com:33804"};

    public  static List<NodeDB> getNodes(Context context){


        List<NodeDB> list=new ArrayList<>();
        list.addAll(getDefaultNodes(context));
        List<NodeDB> dbList= WalletDataBase.getInstance(context).getNodesDao().getAllNodes();
        if(dbList!=null){
            list.addAll(dbList);
        }

        return list;

    }
    public  static List<NodeDB> getDefaultNodes(Context context) {

        List<NodeDB> list = new ArrayList<>();

        list.add(NODE_AUTO);
        if(BuildConfig.DEBUG){
            for (int i = 0; i < TEST_NODES.length; i++) {

                String url = TEST_NODES[i];
                NodeDB node = new NodeDB();

                node.setId(-i-1);
                node.setIp(url.substring(0, url.indexOf(":")));

                node.setPost(url.substring(url.indexOf(":") + 1));
                node.setDefault(true);
                node.setUrl(url);
                node.setTag(context.getString(R.string.str_default_node) + i);

                list.add(node);

            }
        }else {
            for (int i = 0; i < DEFAULT_NODES.length; i++) {

                String url = DEFAULT_NODES[i];
                NodeDB node = new NodeDB();

                node.setId(-i - 1);
                node.setIp(url.substring(0, url.indexOf(":")));

                node.setPost(url.substring(url.indexOf(":") + 1));
                node.setDefault(true);
                node.setUrl(url);
                node.setTag(context.getString(R.string.str_default_node) + i);

                list.add(node);

            }
        }
        return list;
    }


}
