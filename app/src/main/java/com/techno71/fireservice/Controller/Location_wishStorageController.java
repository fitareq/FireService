package com.techno71.fireservice.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Model.AllComment;
import com.techno71.fireservice.Model.BookMarkShow;
import com.techno71.fireservice.Model.LocationWithStorageShow;
import com.techno71.fireservice.R;
import com.techno71.fireservice.View.StorageAddActivity;
import com.techno71.fireservice.View.UserMapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public   class Location_wishStorageController extends RecyclerView.Adapter<Location_wishStorageController.MyviewHolder> {

    private List<LocationWithStorageShow> locationWithStorageShowList;

    private Context context;

    private static int listposition=-1;

    private  String  accessTocken;

    private SharedPreferences sharedPreferences_type;

    private String  storage_add= Main_Url.ROOT_URL +"api/storage-add-comment";
    private String  addBookmark= Main_Url.ROOT_URL +"api/add-bookmark-list";

    private   BottomSheetDialog bottomSheetDialog;
    private String locationId;
    private boolean isCompany;


    public Location_wishStorageController(String locationId, List<LocationWithStorageShow> locationWithStorageShowList, Context context, boolean isCompany) {
        this.locationWithStorageShowList = locationWithStorageShowList;
        this.context = context;
        this.locationId = locationId;
        this.isCompany = isCompany;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view= layoutInflater.inflate(R.layout.location_wish_storage_layout,parent,false);

        return new  MyviewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder myviewHolder, int position) {

        sharedPreferences_type=context.getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);
           accessTocken= sharedPreferences_type.getString("access_token","access_token found");

        LocationWithStorageShow item = locationWithStorageShowList.get(position);

        if (isCompany){
            myviewHolder.addBookmark.setVisibility(View.VISIBLE);
        }

        List<AllComment> comments = item.getAllComments();
        List<String> commentString = new ArrayList<>();
        if (comments != null && !comments.isEmpty()){
            for (AllComment comment: comments){
                commentString.add(comment.getComment());
            }
        }else {
            myviewHolder.userComments.setVisibility(View.GONE);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context,
                com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item,
                commentString
        );

//        myviewHolder.userComments.setAdapter(arrayAdapter);

        myviewHolder.floor.setText(item.getFloor());

        String color = item.getAlert_tag();
        if (color.equalsIgnoreCase("Red"))
        myviewHolder.mainContainer.setBackgroundColor(Color.RED);
        else if (color.equalsIgnoreCase("Green"))
            myviewHolder.mainContainer.setBackgroundColor(Color.GREEN);
        else if (color.equalsIgnoreCase("Yellow"))
            myviewHolder.mainContainer.setBackgroundColor(Color.YELLOW);

        myviewHolder.addBookmark.setOnClickListener(view -> {
            addToBookMark(item.getId());
        });
        myviewHolder.userComments.setOnClickListener(view -> {
            userOwnCommentBottomSheetDialog(arrayAdapter);
        });


        /*myviewHolder.textView_companyName.setText("Name:"+item.getCompany_name());
        myviewHolder.textView_owner.setText("Owner:"+item.getCompany_owner());
        myviewHolder.textView_approvDate.setText("Approv Date:"+item.getLicense_approved_date());
        myviewHolder.textView_revewDate.setText("Revew Date :"+item.getLicense_renew_date());
        myviewHolder.textView_division.setText("Division:"+item.getDivision());
        myviewHolder.textView_alertTag.setText("Alert Tag:"+item.getAlert_tag());
        myviewHolder.textView_location.setText("Location:"+item.getAddress());

        Picasso.get().load(item.getStorage_img()).into(myviewHolder.imageView);

        myviewHolder.floatingactionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setComments(item);
            }
        });*/

        setAnimiton(myviewHolder.itemView,position);

    }

    private void addToBookMark(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, addBookmark, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);

                    if(jsonObjectMain.getString("message").contains("Successfully")){
                        //bottomSheetDialog.dismiss();
                        Toast.makeText(context, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    }else{

                        Toast.makeText(context, ""+jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkError(error);

            }
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("security_error","tec71");
                hashMap.put("location_id",locationId);
                hashMap.put("storage_id",id);
                hashMap.put("axcess_token",accessTocken);
                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void setComments(LocationWithStorageShow item){

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        bottomSheetDialog = new BottomSheetDialog(context);

        View view1=layoutInflater.inflate(R.layout.user_comment,null);
        bottomSheetDialog.setContentView(view1);

        EditText editTextComment = view1.findViewById(R.id.editText_userComment);

        Spinner spinner = view1.findViewById(R.id.spinner_Usercolor);

        Button button_submit=view1.findViewById(R.id.userComment_submit);
        Button button_close=view1.findViewById(R.id.userComment_close);

        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editTextComment.getText().toString().isEmpty()){

                    Toast.makeText(context, "Comment is Empty..", Toast.LENGTH_SHORT).show();
                    editTextComment.setError("Comment is Empty..");

                }else if(spinner.getSelectedItem().toString().contains("Select--Colors--")){

                    Toast.makeText(context, "Colors is Empty", Toast.LENGTH_SHORT).show();

                }else{

                    storage_add_comment(item.getId(),editTextComment.getText().toString().trim(),spinner.getSelectedItem().toString());
                }

            }
        });

        bottomSheetDialog.show();
    }
    void setAnimiton(View viewAnimition,int position) {

        if (position > listposition) {

            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            animation.setDuration(1000);
            viewAnimition.startAnimation(animation);
            listposition = position;
        }
    }


   private void  storage_add_comment(String id,String comment,String alert){

       StringRequest stringRequest = new StringRequest(Request.Method.POST, storage_add, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {

               try {
                   JSONObject jsonObjectMain=new JSONObject(response);

                   if(jsonObjectMain.getString("message").contains("Comment Successfully!")){
                       bottomSheetDialog.dismiss();
                       Toast.makeText(context, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                   }else{

                       Toast.makeText(context, "error:"+jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               netWorkError(error);

           }
       }){
           @Override
           protected Map<String, String> getParams() {

               Map<String, String> hashMap = new HashMap<String, String>();
               hashMap.put("security_error","tec71");
               hashMap.put("store_id",id);
               hashMap.put("axcess_token",accessTocken);
               hashMap.put("comment",comment);
               hashMap.put("alert_tag",alert);
               return hashMap;
           }
       };

       RequestQueue requestQueue= Volley.newRequestQueue(context);
       requestQueue.add(stringRequest);

    }

    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(context, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(context, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(context, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(context, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }


    @Override
    public int getItemCount() {
        return locationWithStorageShowList.size();
    }

    static class  MyviewHolder extends RecyclerView.ViewHolder {

      private TextView textView_companyName,textView_owner,
              textView_location,textView_approvDate,textView_revewDate,
              textView_alertTag,textView_division;

      private ImageView imageView;


      private TextView floor;
      private TextView userComments;

      //floating_comment_storage
      private FloatingActionButton floatingactionbutton;
      private ConstraintLayout mainContainer;
      private FloatingActionButton addBookmark;

       public MyviewHolder(@NonNull View itemView) {
           super(itemView);

           floor = itemView.findViewById(R.id.floor_tv);
           userComments = itemView.findViewById(R.id.own_comment);
           addBookmark = itemView.findViewById(R.id.addBookmark);
           mainContainer= itemView.findViewById(R.id.main_container);

           /*textView_companyName=itemView.findViewById(R.id.text_company_name);
           textView_owner=itemView.findViewById(R.id.text_company_owner);
           textView_approvDate=itemView.findViewById(R.id.text_Company_license_approved_date);
           textView_revewDate=itemView.findViewById(R.id.text_Company_license_renew_date);
           textView_location=itemView.findViewById(R.id.text_CompanyLocation);
           textView_alertTag=itemView.findViewById(R.id.text_company_AlertTag);
           textView_division=itemView.findViewById(R.id.text_companydivision);

           imageView=itemView.findViewById(R.id.company_markImage);
           floatingactionbutton=(FloatingActionButton)itemView.findViewById(R.id.floating_comment_storage);*/
       }
    }

    private void userOwnCommentBottomSheetDialog(ArrayAdapter<String> arrayAdapter) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.user_own_comment);
        ListView listView = bottomSheetDialog.findViewById(R.id.comment_list);
        if (listView != null) {
            listView.setAdapter(arrayAdapter);
            bottomSheetDialog.show();
        }

    }
}
