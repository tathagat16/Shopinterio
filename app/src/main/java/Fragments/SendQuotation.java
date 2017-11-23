package Fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.application.shopinterio.MainActivity;
import com.example.application.shopinterio.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.IntegerRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import static com.example.application.shopinterio.R.id.client_name;

import com.itextpdf.text.pdf.XfaForm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



import static java.lang.Double.valueOf;
import static java.security.AccessController.getContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SendQuotation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SendQuotation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendQuotation extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<String> description;
    ArrayList<String> thickness;
    ArrayList<String> color;
    ArrayList<String> qty;
    ArrayList<String> rate;
    ArrayList<String> amount;
    String timeStamp;
    String docname;


    static double sum = 0;
    static double tax;
    static String gstString="";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SendQuotation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendQuotation.
     */
    // TODO: Rename and change types and number of parameters
    public static SendQuotation newInstance(String param1, String param2) {
        SendQuotation fragment = new SendQuotation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_send_quotation, container, false);
        description =new ArrayList<String>();
        thickness = new ArrayList<String >();
        color = new ArrayList<String>();
        qty = new ArrayList<String>();
        rate = new ArrayList<String>();
        amount = new ArrayList<String>();
        final Spinner spin = (Spinner) view.findViewById(R.id.taxSelect);
       final EditText Desc = (EditText) view.findViewById(R.id.description);
       final EditText Thk = (EditText) view.findViewById(R.id.thk);
       final EditText Color = (EditText) view.findViewById(R.id.color);
       final EditText quty = (EditText) view.findViewById(R.id.qty);
        final EditText ratE = (EditText) view.findViewById(R.id.rate);
        //  EditText amt = (EditText) findViewById(R.id.amt);

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(new Date());
        docname = "Quotation" + timeStamp + ".pdf";



        Button btn = (Button) view.findViewById(R.id.addData);



        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 1){
                    tax = 0.18;
                    gstString="GST 18%";
                    // Toast.makeText(getApplicationContext(),"18 ,Sum: " +sum  + " tax:" + tax ,Toast.LENGTH_SHORT).show();
                }
                else if(position == 2) {
                    tax = 0.28;
                    gstString="GST 28%";
                    // Toast.makeText(getApplicationContext(), "28 ,Sum: " +sum + " tax:" + tax, Toast.LENGTH_SHORT).show();
                }
                else
                    tax=0;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                description.add(Desc.getText().toString());
                thickness.add(Thk.getText().toString());
                color.add(Color.getText().toString());

                int Amt;
                int Qty = 0;
                int Rate1 = 0;

                String Quan = quty.getText().toString();
                String Rate = ratE.getText().toString();

                try {


                    Qty = Integer.parseInt(Quan);
                    Rate1 = Integer.parseInt(Rate);

                } catch (NumberFormatException nfe) {
                }

                Amt = Qty * Rate1;

                amount.add(String.valueOf(Amt));
                qty.add(String.valueOf(Qty));
                rate.add(String.valueOf(Rate1));

                ratE.setText("");
                //  amt.setText("");
                quty.setText("");
                Desc.setText("");
                Thk.setText("");
                Color.setText("");
            }
        });

return view;
    }


    public void createPDF(View view) {

        EditText name = (EditText) view.findViewById(client_name);
        EditText contact = (EditText) view.findViewById(R.id.contact);
        EditText email = (EditText) view.findViewById(R.id.email);
        EditText subject = (EditText) view.findViewById(R.id.subject);


        String Name = name.getText().toString();
        String Contact = contact.getText().toString();
        String Email = email.getText().toString();
        String Subject = subject.getText().toString();
        if (TextUtils.isEmpty(Name)) {
            name.setError("Enter Client Name");
            name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Contact)) {
            contact.setError("Enter Contact");
            contact.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Email)) {
            email.setError("Enter Email");
            email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Subject)) {
            subject.setError("Enter Subject");
            subject.requestFocus();
            return;
        }



        Document doc = new Document();

        //String outPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Download"+ "/" + docname;
        //File outpath = new File (Environment.getExternalStorageDirectory()+ + docname);
        String outPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Quotation";

        PdfWriter writer = null;
        try {
            File dir= new File(outPath);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFcreater", "PDF Path" + outPath);
            File file = new File(dir, docname);
            FileOutputStream fout = new FileOutputStream(file);
            writer = PdfWriter.getInstance(doc, fout);
            doc.open();




            try {


                Drawable d = getResources().getDrawable(R.drawable.top);

                BitmapDrawable bitDw = ((BitmapDrawable) d);

                Bitmap bmp = bitDw.getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                Image image = Image.getInstance(stream.toByteArray());

                doc.add(image);


            } catch (Exception e) {
                e.printStackTrace();
            }


            doc.add(createFirstTable(description, Name, Contact, Email, Subject, thickness, color, qty, rate, amount));


            try {


                Drawable d = getResources().getDrawable(R.drawable.bottom);

                BitmapDrawable bitDw = ((BitmapDrawable) d);

                Bitmap bmp = bitDw.getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                Image image = Image.getInstance(stream.toByteArray());

                doc.add(image);


            } catch (Exception e) {
                e.printStackTrace();
            }

           /* PdfReader reader = new PdfReader(outPath);
            int n = reader.getNumberOfPages();
            PdfImportedPage page;
            for (int i = 1; i <= n; i++) {
                // Only page number 2 will be included
                if (i == 1) {
                    page = writer.getImportedPage(reader, i);
                    Image.getInstance(page);
                    // here you can show image on your phone
                }
            }*/


            doc.close();

            Toast.makeText(getContext(), "Created", Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }





    public static PdfPTable createFirstTable(ArrayList<String> description, String Name, final String Contact, String Email, String Subject, ArrayList<String> thickness, ArrayList<String> color, ArrayList<String> qty, ArrayList<String> rate, ArrayList<String> amount ) {

        String Date = new SimpleDateFormat("dd/MM/YYYY", Locale.ROOT).format(new Date());
        String Quote = new SimpleDateFormat("YYYYMMddHHmmss", Locale.ROOT).format(new Date());

        PdfPTable table = new PdfPTable(10); //total colums goes here
        PdfPCell cell;
        table.setWidthPercentage(100);


        cell = new PdfPCell(new Phrase("Quotation"));
        cell.setColspan(10);
        table.addCell(cell);


        //////////////////////////////////
        cell = new PdfPCell(new Phrase("Date: " + Date ));
        cell.setColspan(5);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase("Client Name: " + Name));
        cell.setColspan(5);
        table.addCell(cell);


        //////////////////////////////////
        cell = new PdfPCell(new Phrase("Quote number:  RM- "+ Quote));
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Mobile No.: "+ Contact ));
        cell.setColspan(5);
        table.addCell(cell);


        //////////////////////////////////
        cell = new PdfPCell(new Phrase("Company Name and address: "));
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Email Id: " +Email ));
        cell.setColspan(5);
        table.addCell(cell);


        //////////////////////////////////
        cell = new PdfPCell(new Phrase("Tanmay Traders "));
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Site Address"));
        cell.setColspan(5);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase("GSTIN-"));
        cell.setColspan(5);
        cell.setMinimumHeight(30f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(5);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase("Sub: " +Subject ));
        cell.setColspan(10);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase("Project Name: "));
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Dear Sir,"));
        cell.setColspan(10);
        table.addCell(cell);


        table.addCell("S.no");

        cell = new PdfPCell(new Phrase("Description"));
        cell.setColspan(3);
        table.addCell(cell);

        table.addCell("Thk.");

        cell = new PdfPCell(new Phrase("Color"));
        cell.setColspan(2);
        table.addCell(cell);

        table.addCell("Quantity per Sqft");
        table.addCell("Rate Per Sqft");

        table.addCell("Amount");


//        int n = 1; //NO OF ROWS COMES HERE
        for (int i = 0; i < description.size(); i++) {

            table.addCell(+i + 1 + ".");

            cell = new PdfPCell(new Phrase(description.get(i)));
            cell.setColspan(3);
            table.addCell(cell);

            table.addCell(thickness.get(i));

            cell = new PdfPCell(new Phrase(color.get(i)));
            cell.setColspan(2);
            table.addCell(cell);

            table.addCell(qty.get(i));
            table.addCell(rate.get(i));

            table.addCell(amount.get(i));
        }

        table.addCell("");

        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(3);
        table.addCell(cell);

        table.addCell("");

        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Total"));
        cell.setColspan(2);
        table.addCell(cell);



        for(int i = 0; i < amount.size(); i++)
        {
            sum = sum + Integer.parseInt(amount.get(i));
            if(i==amount.size()-1)
                tax=valueOf(tax) *sum;
        }
        table.addCell(String.valueOf(sum));



        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(5);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(gstString));
        cell.setColspan(4);
        table.addCell(cell);



        table.addCell(String.valueOf(tax));


        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Amount inc GST"));
        cell.setColspan(4);
        table.addCell(cell);

        table.addCell(String.valueOf(sum+tax));


        return table;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
