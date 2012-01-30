
package jp.tomorrowkey.android.androidcolorviewer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends ListActivity {

    public static final String LOG_TAG = HomeActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        List<NamedColor> colors = new ArrayList<HomeActivity.NamedColor>();
        try {
            for (Field field : Class.forName("android.R$color").getDeclaredFields()) {
                String name = field.getName();
                int id = field.getInt(null);
                int color = getResources().getColor(id);
                colors.add(new NamedColor(name, color));
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        setListAdapter(new NamedColorAdapter(this, colors));
    }

    static class NamedColor {

        public String name;

        public int color;

        public NamedColor(String name, int color) {
            this.name = name;
            this.color = color;
        }
    }

    static class NamedColorAdapter extends BaseAdapter {

        private Context mContext;

        private LayoutInflater mInflater;

        private List<NamedColor> mColors;

        public NamedColorAdapter(Context context, List<NamedColor> colors) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mColors = colors;
        }

        @Override
        public int getCount() {
            return mColors.size();
        }

        @Override
        public Object getItem(int position) {
            return mColors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = mInflater.inflate(R.layout.row, null, false);
                viewHolder = new ViewHolder();
                viewHolder.colorImageView = (ImageView)view.findViewById(R.id.color_image_view);
                viewHolder.colorNameTextView = (TextView)view
                        .findViewById(R.id.color_name_textview);
                view.setTag(viewHolder);

                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                        R.drawable.white_square);
                viewHolder.colorImageView.setImageBitmap(bitmap);
            } else {
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }

            NamedColor namedColor = (NamedColor)getItem(position);
            viewHolder.colorImageView.setColorFilter(namedColor.color, PorterDuff.Mode.SRC_ATOP);
            viewHolder.colorNameTextView.setText(namedColor.name);

            return view;
        }

        class ViewHolder {
            ImageView colorImageView;

            TextView colorNameTextView;
        }
    }
}
