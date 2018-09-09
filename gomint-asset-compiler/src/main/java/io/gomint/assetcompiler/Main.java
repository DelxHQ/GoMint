package io.gomint.assetcompiler;

import io.gomint.taglib.NBTTagCompound;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author geNAZt
 * @version 1.0
 */
public class Main {

    public static void main( String[] args ) throws IOException {
        // Load the MITM assets
        Set<String> knownIDs = new HashSet<>();
        NBTTagCompound assetsCompound = null;

        try {
            assetsCompound = NBTTagCompound.readFrom( new File( "gomint-asset-compiler/input-from-mitm.dat" ), true, ByteOrder.BIG_ENDIAN );
            for ( Object palette : assetsCompound.getList( "blockPalette", false ) ) {
                NBTTagCompound entry = (NBTTagCompound) palette;
                knownIDs.add( entry.getString( "id", null ) );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
            System.exit( -1 );
        }

        // First we need to cleanup the id converter map

        /*
         * Short format stuff on how the id converter data works
         *
         * The ID converter takes a anvil 1.8 stored block id and converts it to the correct new string block id.
         * It also converts metadata from one stage to another (like facing which are different in PE)
         *
         * One line represents one converter pair:
         *
         * oldid [oldmeta or -1 for all] newid [newmeta or -1 for direct passthrough (only works when oldmeta is -1)]
         *
         * This compiler takes in the WIP-id-converter-map.txt from planet minecraft (thx for that), strips out the
         * data i don't need, uses the old metadata converted from Gomint to map oldmeta to newmeta then pushes it
         * into a output-id-converter-map.txt for control.
         */
        Map<Integer, String> oldToNewId = new HashMap<>();

        try {
            List<String> input = Files.readAllLines( Paths.get( "gomint-asset-compiler/WIP-id-converter-map.txt" ) );
            for ( String idLine : input ) {
                int oldId;
                String newId;

                String[] split = idLine.split( " " );
                if ( split.length > 2 ) {
                    oldId = Integer.parseInt( split[0] );
                    newId = split[2];
                } else {
                    oldId = Integer.parseInt( split[0] );
                    newId = split[1];
                }

                if ( !knownIDs.contains( newId ) ) {
                    System.out.println( oldId + " -> " + newId );
                }

                oldToNewId.put( oldId, newId );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }


        // Build up the real converter map
        DataConverter dataConverter = new DataConverter();
        try ( BufferedWriter writer = new BufferedWriter( new FileWriter( "gomint-asset-compiler/output-id-converter-map.txt" ) ) ) {
            for ( Map.Entry<Integer, String> entry : oldToNewId.entrySet() ) {
                // Check if data converter has a entry
                if ( dataConverter.hasConverter( entry.getKey() ) ) {
                    for ( byte i = 0; i < 16; i++ ) {
                        writer.write( entry.getKey() + " " + i + " " + entry.getValue() + " " + dataConverter.convert( entry.getKey(), i ).getSecond() + "\n" );
                    }
                } else {
                    writer.write( entry.getKey() + " -1 " + entry.getValue() + " -1\n" );
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        // Wait for user to control the output id converter
        try {
            System.out.println( "Please check the output-id-converter-map.txt file and correct it if needed. Press enter to continue..." );
            System.in.read();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        // We read the output again so we know if the user has changed something
        List<Object> converters = assetsCompound.getList( "converter", true );
        try {
            List<String> input = Files.readAllLines( Paths.get( "gomint-asset-compiler/output-id-converter-map.txt" ) );
            for ( String idPair : input ) {
                String[] split = idPair.split( " " );

                NBTTagCompound idPairCompound = new NBTTagCompound( "" );
                idPairCompound.addValue( "oi", Short.parseShort( split[0] ) );
                idPairCompound.addValue( "om", Byte.parseByte( split[1] ) );
                idPairCompound.addValue( "ni", split[2] );
                idPairCompound.addValue( "nm", Byte.parseByte( split[3] ) );

                converters.add( idPairCompound );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        // Write the final assets.dat
        assetsCompound.writeTo( new File( "gomint-asset-compiler/assets.dat" ), true, ByteOrder.BIG_ENDIAN );
    }

}