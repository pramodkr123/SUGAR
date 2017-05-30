package com.example.peter.sugar;

/**
 * Created by Peter on 19.05.2017.
 */

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import java.io.*;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import java.util.*;

public class BlackList {

    private XmlSerializer xmlWriter;
    private ArrayList<Profile> activatedProfiles;
    private ArrayList<String> blockedNumbers;

    public BlackList()
    {
        activatedProfiles = new ArrayList<Profile>(0);
        blockedNumbers = new ArrayList<String>(0);
    }

    public void addProfile(Profile addProfile,Context context) throws Exception
    {
        activatedProfiles.add(addProfile);
        for(ListIterator<String> currentProfileIterator = addProfile.getPhoneNumbers().listIterator(); currentProfileIterator.hasNext();)
        {
            blockedNumbers.add(currentProfileIterator.next());
        }
        saveBlockList(context);
    }

    public boolean removeProfile( Profile removeProfile , Context context ) throws Exception
    {
        int currentArrayListId = 0;
        for(ListIterator<Profile> iterator = activatedProfiles.listIterator();iterator.hasNext();)
        {
            Profile currentProfile = iterator.next();
            if( currentProfile.getId() == removeProfile.getId() )
            {
                ArrayList<String> buffer = currentProfile.getPhoneNumbers();
                for(ListIterator<String> iteratorNumbers = buffer.listIterator(); iteratorNumbers.hasNext();)
                {
                    blockedNumbers.remove(iteratorNumbers.next());
                }
                activatedProfiles.remove(currentArrayListId);
                saveBlockList(context);
                return true;
            }
        }
        return false;
    }

    public boolean readBlackList(Context context) throws IOException,XmlPullParserException
    {
        try {
            BlockListParser parser = new BlockListParser();
            ArrayList<String> result = parser.parse(context.openFileInput("blacklist.xml"), context);
            blockedNumbers = result;
            return true;
        } catch ( IOException e ) {
            Log.d("READ BLACKLIST : "," Unfortunately your blacklist couldn't be read : " + e.getMessage());
            return false;
        } catch ( XmlPullParserException e ) {
            Log.d("READ XML : ","Unfortunately your xml file containing the blacklist is badly formatted : " + e.getMessage());
            return false;
        }
    }

    public boolean saveBlockList(Context context) throws Exception
    {
        File currentBlacklistFile = new File(context.getFilesDir() + "blacklist.xml");
        if( currentBlacklistFile.exists() )
            currentBlacklistFile.delete();
        FileOutputStream fileOutput = null;
        try {
            fileOutput = context.openFileOutput("blacklist.xml",Context.MODE_PRIVATE);
            xmlWriter = Xml.newSerializer();
            xmlWriter.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output",true);
            xmlWriter.setOutput(fileOutput,"UTF-8");
            xmlWriter.startDocument("UTF-8",true);
            xmlWriter.startTag(null,"blacklist");
            xmlWriter.startTag(null,"numbers");
            if(!(getBlockedNumbers().size() == 0) ) {
                for (ListIterator<String> phoneNumberIterator = blockedNumbers.listIterator(); phoneNumberIterator.hasNext(); ) {
                    xmlWriter.text(phoneNumberIterator.next() + ",");
                }
            } else if ( getBlockedNumbers().size() == 0 ) {
                xmlWriter.text("");
            }
            xmlWriter.endTag(null,"numbers");
            xmlWriter.endTag(null,"blacklist");
            xmlWriter.endDocument();
            xmlWriter.flush();
            return true;
        } catch ( Exception e ) {
            Log.d("WRITE BLACKLIST :","Unfortunately your blacklist could not be written : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String> getBlockedNumbers()
    {
        return blockedNumbers;
    }

    public ArrayList<Profile> getActivatedProfiles()
    {
        return activatedProfiles;
    }

}