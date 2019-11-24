package com.example.planb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

    public class guide_adapter extends RecyclerView.Adapter<guide_adapter.ViewHolder> {
    private ArrayList<guide> listData = new ArrayList<>();
    Context context;
    OnItemClickListener listener;
    public  static interface  OnItemClickListener{
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    public guide_adapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @NonNull
    @Override //뷰홀더가 만들어지는 시점에 호출되는 메소드(각각의 아이템을 위한 뷰홀더 객체가 처음만들어지는시점)
    //만약에 각각의 아이템을 위한 뷰홀더가 재사용될 수 있는 상태라면 호출되지않음 (그래서 편리함, 이건내생각인데 리스트뷰같은경우는 convertView로 컨트롤해줘야하는데 이건 자동으로해줌)
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.guideframe,  viewGroup, false);//viewGroup는 각각의 아이템을 위해서 정의한 xml레이아웃의 최상위 레이아우싱다.

        return new ViewHolder(itemView); //각각의 아이템을 위한 뷰를 담고있는 뷰홀더객체를 반환한다.(각 아이템을 위한 XML 레이아웃을 이용해 뷰 객체를 만든 후 뷰홀더에 담아 반환
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) {
        guide item = listData.get(position); //리사이클러뷰에서 몇번쨰게 지금 보여야되는시점이다 알려주기위해
        viewholder.setItem(item); //그거를 홀더에넣어서 뷰홀더가 데이터를 알 수 있게되서 뷰홀더에 들어가있는 뷰에다가 데이터 설정할 수 있음
        //클릭리스너
        viewholder.setOnItemClickListener(listener);
    }
    public void addItem(guide g){
        listData.add(g);
    }
    public void addItems(ArrayList<guide> items){
        this.listData = items;
    }
    public guide getItem(int position){
        return listData.get(position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView guideface;
        TextView guidename;
        TextView guidecost;
        TextView guidecont;
        TextView guidedesc;

        OnItemClickListener listener; //클릭이벤트처리관련 변수

        public ViewHolder(@NonNull final View itemView) { //뷰홀더는 각각의 아이템을 위한 뷰를 담고있다.
            super(itemView);

            guideface = itemView.findViewById(R.id.guideface);
            guidename = itemView.findViewById(R.id.guidename);
            guidecost = itemView.findViewById(R.id.guidecost);
            guidecont = itemView.findViewById(R.id.guidecont);
            guidedesc = itemView.findViewById(R.id.guidedesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, itemView, position);
                    }
                }
            });
        }

        /*이거 절대 추가해*/
        public void setItem(guide item) {
            guidecost.setText(item.getPrice());
            guidecont.setText(item.getEmail());
            guidedesc.setText(item.getDesc());
        }
        ///////////////////////////////////////////////////////////////////////////
        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
    }
}