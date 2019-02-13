package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.leese.beer.R;

import java.util.List;

import model.Bebida;

public class AdapterListaOrdenada extends BaseAdapter {

    private Context context;
    private List<Bebida> bebidaList;

    public AdapterListaOrdenada(Context context, List<Bebida> bebida) {
        this.context = context;
        this.bebidaList = bebida;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //o tamanho da lista, quantas bebidas temos
        return this.bebidaList.size();
    }

    @Override
    public Object getItem(int position) {
        // retorna uma bebida em uma certa posicao
        return this.bebidaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removerBebida(int position) {
        this.bebidaList.remove(position);
        notifyDataSetChanged();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(this.context, R.layout.layout_bebidas, null);

        TextView tvNomeBebida  = (TextView) v.findViewById(R.id.tvNomeBebida);
        TextView tvPrecoBebida = (TextView) v.findViewById(R.id.tvPrecoBebida);
        TextView tvMililitros = (TextView) v.findViewById(R.id.tvMililitros);
        TextView tvEstabelecimento = (TextView) v.findViewById(R.id.tvEstabelecimento);

        //NomeBebida é o nome do fabricante, preguiça de mudar no momento

        tvNomeBebida.setText(this.bebidaList.get(position).getFabricante());
        tvPrecoBebida.setText(String.valueOf(this.bebidaList.get(position).getPreco()));
        tvEstabelecimento.setText(this.bebidaList.get(position).getEstabelecimento());
        tvMililitros.setText(String.valueOf(this.bebidaList.get(position).getMililitros()));

        return v;
    }

    //atualiza a lista de produtos
    public void atualizar(List<Bebida> newList){
        this.bebidaList.clear();
        this.bebidaList = newList;
        notifyDataSetChanged();
    }
}
