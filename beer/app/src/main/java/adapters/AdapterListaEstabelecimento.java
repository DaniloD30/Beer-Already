package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.leese.beer.R;

import java.util.List;

import model.Bebida;
import model.Estabelecimento;

public class AdapterListaEstabelecimento extends BaseAdapter {

    private Context context;
    private List<Estabelecimento> estabelecimentoList;

    public AdapterListaEstabelecimento(Context context,List<Estabelecimento>  estabelecimento) {
        this.context = context;
        this.estabelecimentoList = estabelecimento;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //o tamanho da lista, quantas bebidas temos
        return this.estabelecimentoList.size();
    }

    @Override
    public Object getItem(int position) {
        // retorna uma bebida em uma certa posicao
        return this.estabelecimentoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removerBebida(int position) {
        this.estabelecimentoList.remove(position);
        notifyDataSetChanged();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(this.context, R.layout.layout_estabelecimento, null);

        TextView tvNomeEstabelecimento  = (TextView) v.findViewById(R.id.tvNome);
        TextView tvEndereco = (TextView) v.findViewById(R.id.tvEndereco);


        //NomeBebida é o nome do fabricante, preguiça de mudar no momento

        tvNomeEstabelecimento.setText(this.estabelecimentoList.get(position).getNome());
        tvEndereco.setText(String.valueOf(this.estabelecimentoList.get(position).getEndereço()));


        return v;
    }

    //atualiza a lista de produtos
    public void atualizar(List<Estabelecimento> newList){
        this.estabelecimentoList.clear();
        this.estabelecimentoList = newList;
        notifyDataSetChanged();
    }
}
