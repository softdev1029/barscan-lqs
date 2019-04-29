package com.softdev.barcodescanner.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdev.barcodescanner.ScanActivity;
import com.softdev.barcodescanner.ScanArticleActivity;
import com.softdev.barcodescanner.ScanDetailActivity;
import com.softdev.barcodescanner.UserInfoActivity;
import com.softdev.barcodescanner.models.Barcode;
import com.softdev.barcodescanner.R;
import com.softdev.barcodescanner.models.Store;
import com.softdev.barcodescanner.utils.Constant;


import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Set;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.ViewHolder> {
    private HashMap<Integer, Barcode> mDataset;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView barcodeView;
        public Button btnDetail;
        public Button btnDelete;

        public ViewHolder(View v) {
            super(v);
            barcodeView = v.findViewById(R.id.tv_barcode);
            btnDetail = v.findViewById(R.id.button_detail);
            btnDelete = v.findViewById(R.id.button_delete);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BarcodeAdapter(Context context, HashMap<Integer, Barcode> dataset) {
        mContext = context;
        mDataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BarcodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_barcode, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Set<Integer> keySet = mDataset.keySet();
        final Integer key = (Integer) (mDataset.keySet().toArray())[position];
        final Barcode code = mDataset.get(key);

        holder.barcodeView.setText(code.getBarcode());
        holder.barcodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ScanArticleActivity.class);
                intent.putExtra(Constant.ACTION_NAME, code.getActionType());
                intent.putExtra(Constant.BAG_KEY, key);
                mContext.startActivity(intent);
            }
        });
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Store.setCurrentBarcode(code);
                Intent intent = new Intent(mContext, ScanDetailActivity.class);
                intent.putExtra(Constant.ACTION_NAME, code.getActionType());
                mContext.startActivity(intent);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage(R.string.msg_delete)
                        .setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDataset.remove(key);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.lbl_no, null)
                        .show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}