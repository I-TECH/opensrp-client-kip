package org.smartregister.kip.provider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.smartregister.kip.R;
import org.smartregister.kip.adapter.StockProviderForCursorAdapter;
import org.smartregister.kip.domain.Stock;
import org.smartregister.kip.repository.StockRepository;
import org.smartregister.view.contract.SmartRegisterClients;
import org.smartregister.view.dialog.FilterOption;
import org.smartregister.view.dialog.ServiceModeOption;
import org.smartregister.view.dialog.SortOption;
import org.smartregister.view.viewholder.OnClickFormLauncher;

import java.util.Date;

import util.JsonFormUtils;

/**
 * Created by Raihan  on 29-05-17.
 */
public class StockRowSmartClientsProvider implements StockProviderForCursorAdapter {
    private final LayoutInflater inflater;
    private final StockRepository stockRepository;

    public StockRowSmartClientsProvider(Context context,
                                        StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void getView(Stock stock, View convertView) {

        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView to_from = (TextView) convertView.findViewById(R.id.to_from);
        TextView received = (TextView) convertView.findViewById(R.id.received);
        TextView issued = (TextView) convertView.findViewById(R.id.issued);
        TextView loss_adj = (TextView) convertView.findViewById(R.id.loss_adj);
        TextView balance = (TextView) convertView.findViewById(R.id.balance);


        if (stock.getTransactionType().equalsIgnoreCase(Stock.received)) {
            received.setText("" + stock.getValue());
            issued.setText("");
            loss_adj.setText("");
        }
        if (stock.getTransactionType().equalsIgnoreCase(Stock.issued)) {
            received.setText("");
            issued.setText("" + (-1 * stock.getValue()));
            loss_adj.setText("");
        }
        if (stock.getTransactionType().equalsIgnoreCase(Stock.loss_adjustment)) {
            received.setText("");
            issued.setText("");
            loss_adj.setText("" + stock.getValue());
        }

        date.setText(JsonFormUtils.dd_MM_yyyy.format(new Date(stock.getDateCreated())));
        to_from.setText(stock.getToFrom().replace("_", " "));

        balance.setText("" + (stock.getValue() + stockRepository.getBalanceBeforeCheck(stock)));


    }

    @Override
    public SmartRegisterClients updateClients(FilterOption villageFilter, ServiceModeOption
            serviceModeOption, FilterOption searchFilter, SortOption sortOption) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public void onServiceModeSelected(ServiceModeOption serviceModeOption) {

    }

    @Override
    public OnClickFormLauncher newFormLauncher(String formName, String entityId, String
            metaData) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public View inflatelayoutForCursorAdapter() {
        return (ViewGroup) inflater().inflate(R.layout.smart_register_stock_control_client, null);
    }

    public LayoutInflater inflater() {
        return inflater;
    }


}