package com.year3project.qrcodegenerator;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class GetAnnouncement extends AsyncTask<Void, Void, ArrayList<AnnouncementModel>> {

    private static final String TAG = "GetAnnouncement";

    private Context context;
    private RecyclerView rv;
    private String selectedDepartment;
    private ProgressDialog progressDialog;

    public GetAnnouncement(Context context, RecyclerView rv, String selectedDepartment) {
        this.context = context;
        this.rv = rv;
        this.selectedDepartment = selectedDepartment;
    }

    private ArrayList<AnnouncementModel> convertJSONArrayToArrayList(JSONArray resultArray) {
        try {
            ArrayList<AnnouncementModel> announcements = new ArrayList<>();
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject result = resultArray.getJSONObject(i);
                String title = result.getString("title");
                String message = result.getString("message");
                String date = result.getString("date");
                announcements.add(new AnnouncementModel(title, message, date));
            }
            return announcements;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Fetch Announcements");
        progressDialog.setMessage("Fetching Announcement... Please wait");
        progressDialog.show();
    }

    @Override
    protected ArrayList<AnnouncementModel> doInBackground(Void... voids) {
        String uploadURL = "https://pucls.000webhostapp.com/php/get_announcement_by_department.php";
        try {
            URL url = new URL(uploadURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("department", "UTF-8")+"="+URLEncoder.encode(selectedDepartment, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = bufferedReader.readLine();

            JSONArray resultArray = new JSONArray(result);
            ArrayList<AnnouncementModel> announcementModelArrayList = new ArrayList<>();
            announcementModelArrayList = convertJSONArrayToArrayList(resultArray);

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            return announcementModelArrayList;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<AnnouncementModel> announcementModelArrayList) {
        progressDialog.dismiss();
        AnnouncementAdapter announcementAdapter = new AnnouncementAdapter(context, announcementModelArrayList);
        rv.setAdapter(announcementAdapter);
        if (announcementModelArrayList.size() == 0) {
            Toast.makeText(context, "No announcement", Toast.LENGTH_LONG).show();
        }
    }
}
