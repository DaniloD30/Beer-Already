package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.leese.beer.R;

import java.util.List;

import model.Cesta;

public class AdapterCestas extends BaseAdapter {

    private Context context;
    private List<Cesta> cestaList;

    public AdapterCestas(Context context, List<Cesta> bebida) {
        this.context = context;
        this.cestaList = bebida;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //o tamanho da lista, quantas bebidas temos
        return this.cestaList.size();
    }

    @Override
    public Object getItem(int position) {
        // retorna uma bebida em uma certa posicao
        return this.cestaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removerBebida(int position) {
        this.cestaList.remove(position);
        notifyDataSetChanged();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(this.context, R.layout.layout_cestas, null);

        TextView tvNome  = (TextView) v.findViewById(R.id.tvNome);
        tvNome.setText(this.cestaList.get(position).getNome());
        return v;
    }

    public void atualizar(List<Cesta> newList){
        this.cestaList.clear();
        this.cestaList = newList;
        notifyDataSetChanged();
    }

}
