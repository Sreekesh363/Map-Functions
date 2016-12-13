package mappins.sreekesh.com.mappins;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by sree on 13/12/16.
 */

public class DetailsFragment extends Fragment {

    public interface DetailsFragmentCallbacks{
        void zoomToPin(LatLng position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.details_fragment_layout, container,
                false);
        Bundle bundle = getArguments();
        final Double latitude = bundle.getDouble("latitude");
        final Double longitude = bundle.getDouble("longitude");
        String name = bundle.getString("name");
        String address = bundle.getString("address");
        TextView nameView = (TextView) rootView.findViewById(R.id.name);
        TextView latView = (TextView) rootView.findViewById(R.id.latitude);
        TextView lngView = (TextView) rootView.findViewById(R.id.longitude);
        TextView addressView = (TextView) rootView.findViewById(R.id.address);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);
        nameView.setText(name);
        latView.setText(String.valueOf(latitude));
        lngView.setText(String.valueOf(longitude));
        addressView.setText(address);
        String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=15&size=350x150&scale=2&sensor=false&markers=color:0x837494%7C" + latitude + "," + longitude;
        Glide.with(getActivity())
                .load(url).signature(new StringSignature("use unique refresh key"))
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DetailsFragmentCallbacks) getActivity()).zoomToPin(new LatLng(latitude,longitude));
            }
        });
        return rootView;
    }
}
