/*
 * Copyright (c) 2017, GoMint, BlackyPaw and geNAZt
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.world.anvil;

import io.gomint.math.BlockPosition;
import io.gomint.server.entity.tileentity.TileEntity;
import io.gomint.server.util.Pair;
import io.gomint.server.world.ChunkAdapter;
import io.gomint.server.world.NibbleArray;
import io.gomint.server.world.WorldLoadException;
import io.gomint.server.world.postprocessor.PistonPostProcessor;
import io.gomint.taglib.NBTStream;
import io.gomint.taglib.NBTTagCompound;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author BlackyPaw
 * @version 1.0
 */
@EqualsAndHashCode( callSuper = true )
public class AnvilChunkAdapter extends ChunkAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger( AnvilChunkAdapter.class );
    private static final DataConverter CONVERTER = new DataConverter();

    private boolean converted;
    private boolean invalid;

    // Temporary variables for loading
    private int maxHeight = 0;
    private List<NBTTagCompound> tileEntityHolders;
    private List<NBTTagCompound> entityHolders;
    private List<NBTTagCompound> sections;

    /**
     * Load a Chunk from a NBTTagCompound. This is used when loaded from a Regionfile.
     *
     * @param worldAdapter       which loaded this chunk
     * @param x                  position of chunk
     * @param z                  position of chunk
     * @param lastSavedTimestamp timestamp of last save
     */
    public AnvilChunkAdapter( AnvilWorldAdapter worldAdapter, int x, int z, long lastSavedTimestamp ) {
        super( worldAdapter, x, z );
        this.lastSavedTimestamp = lastSavedTimestamp;
        this.loadedTime = worldAdapter.getServer().getCurrentTickTime();
        this.converted = worldAdapter.isOverrideConverter();
    }

    // ==================================== I/O ==================================== //

    /**
     * Writes the chunk's raw NBT data to the given output stream.
     *
     * @param out The output stream to write the chunk data to.
     * @throws IOException Thrown in case the chunk could not be stored
     */
    void saveToNBT( OutputStream out ) throws IOException {
        LOGGER.debug( "Writing Anvil chunk {}", this );

        NBTTagCompound chunk = new NBTTagCompound( "" );

        NBTTagCompound level = new NBTTagCompound( "Level" );
        level.addValue( "LightPopulated", (byte) 1 );
        level.addValue( "TerrainPopulated", (byte) 1 );
        level.addValue( "V", (byte) 1 );
        level.addValue( "xPos", this.x );
        level.addValue( "zPos", this.z );
        level.addValue( "InhabitedTime", this.inhabitedTime );
        level.addValue( "LastUpdate", 0L );
        level.addValue( "Biomes", this.biomes );
        level.addValue( "GoMintConverted", (byte) 1 );

        List<NBTTagCompound> sections = new ArrayList<>( 8 );

        for ( int sectionY = 0; sectionY < 16; ++sectionY ) {
            byte[] blocks = new byte[4096];
            NibbleArray data = new NibbleArray( (short) 4096 );
            int baseIndex = sectionY * 16;

            for ( int y = baseIndex; y < baseIndex + 16; ++y ) {
                for ( int x = 0; x < 16; ++x ) {
                    for ( int z = 0; z < 16; ++z ) {
                        short blockIndex = (short) ( ( y - baseIndex ) << 8 | z << 4 | x );

                        byte blockId = (byte) this.getBlock( x, y, z );
                        byte blockData = this.getData( x, y, z );

                        blocks[blockIndex] = blockId;
                        data.set( blockIndex, blockData );
                    }
                }
            }

            NBTTagCompound section = new NBTTagCompound( "" );
            section.addValue( "Y", (byte) sectionY );
            section.addValue( "Blocks", blocks );
            section.addValue( "Data", data.raw() );
            sections.add( section );
        }

        level.addValue( "Sections", sections );
        level.addValue( "Entities", new ArrayList( 0 ) );

        List<NBTTagCompound> tileEntityCompounds = new ArrayList<>();
        for ( TileEntity tileEntity : this.getTileEntities() ) {
            NBTTagCompound compound = new NBTTagCompound( "" );
            tileEntity.toCompound( compound );
            tileEntityCompounds.add( compound );
        }

        level.addValue( "TileEntities", tileEntityCompounds );

        chunk.addChild( level );
        chunk.writeTo( out, false, ByteOrder.BIG_ENDIAN );
    }

    /**
     * Loads the chunk from the specified NBTTagCompound
     *
     * @param nbtStream The stream which loads the chunk
     */
    // CHECKSTYLE:OFF
    void loadFromNBT( NBTStream nbtStream ) throws WorldLoadException {
        // Fill in default values
        this.biomes = new byte[256];
        Arrays.fill( this.biomes, (byte) -1 );

        // Allow for compound return for given paths
        nbtStream.addCompountAcceptor( path -> path.equals( ".Level.Entities" ) ||
            path.equals( ".Level.TileEntities" ) ||
            path.startsWith( ".Level.Sections." ) );

        // Attach listener for NBT objects
        nbtStream.addListener( ( path, object ) -> {
            switch ( path ) {
                case ".Level.xPos":
                    int xPos = (int) object;
                    if ( AnvilChunkAdapter.this.x != xPos ) {
                        AnvilChunkAdapter.this.invalid = true;
                    }

                    break;
                case ".Level.zPos":
                    int zPos = (int) object;
                    if ( AnvilChunkAdapter.this.z != zPos ) {
                        AnvilChunkAdapter.this.invalid = true;
                    }

                    break;
                case ".Level.Biomes":
                    AnvilChunkAdapter.this.biomes = (byte[]) object;
                    break;
                case ".Level.InhabitedTime":
                    AnvilChunkAdapter.this.inhabitedTime = (long) object;
                    break;
                case ".Level.GoMintConverted":
                    AnvilChunkAdapter.this.converted = true;
                    break;
                case ".Level.Entities":
                    AnvilChunkAdapter.this.entityHolders = (List<NBTTagCompound>) object;
                    break;
                case ".Level.TileEntities":
                    AnvilChunkAdapter.this.tileEntityHolders = (List<NBTTagCompound>) object;
                    break;
                case ".Level.HeightMap":
                    LOGGER.debug( "" + object );
                    break;
                case ".Level.LastUpdate":
                case ".Level.LightPopulated":
                case ".Level.TerrainPopulated":
                case ".Level.V":
                    break;
                default:
                    if ( path.startsWith( ".Level.Sections" ) ) {
                        // Sections are always read from bottom to top
                        NBTTagCompound compound = (NBTTagCompound) object;
                        if ( AnvilChunkAdapter.this.sections == null ) {
                            AnvilChunkAdapter.this.sections = new ArrayList<>();
                        }

                        AnvilChunkAdapter.this.sections.add( compound );
                    } else {
                        LOGGER.debug( "New path: {}", path );
                    }
            }
        } );

        // Start parsing the nbt tag
        try {
            nbtStream.parse();
        } catch ( Exception e ) {
            LOGGER.error( "Error whilst parsing chunk nbt: ", e );
        }

        if ( this.invalid ) {
            throw new WorldLoadException( "Position stored in chunk does not match region file offset position" );
        }

        if ( this.sections != null ) {
            for ( NBTTagCompound section : this.sections ) {
                int sectionY = section.getByte( "Y", (byte) 0 ) << 4;

                if ( sectionY > this.maxHeight ) {
                    this.maxHeight = sectionY;
                }

                this.loadSection( sectionY, section );
            }

            this.sections = null;
        }

        // Load tile entities
        if ( this.tileEntityHolders != null && !this.tileEntityHolders.isEmpty() ) {
            for ( NBTTagCompound tileEntity : this.tileEntityHolders ) {
                String id = tileEntity.getString( "id", "" );
                switch ( id ) {
                    case "Sign":
                        TileEntityConverter.cleanSignText( tileEntity, "Text1" );
                        TileEntityConverter.cleanSignText( tileEntity, "Text2" );
                        TileEntityConverter.cleanSignText( tileEntity, "Text3" );
                        TileEntityConverter.cleanSignText( tileEntity, "Text4" );
                        break;

                    case "Skull":
                        // Remove the owner or extra data
                        if ( tileEntity.containsKey( "Owner" ) ) {
                            tileEntity.remove( "Owner" );
                        }
                        break;

                    case "RecordPlayer":
                        tileEntity.addValue( "id", "Music" );
                        tileEntity.addValue( "note", (byte) 0 );

                        if ( tileEntity.containsKey( "Record" ) ) {
                            tileEntity.remove( "Record" );
                        }

                        if ( tileEntity.containsKey( "RecordItem" ) ) {
                            tileEntity.remove( "RecordItem" );
                        }

                        break;

                    case "Banner":
                    case "Airportal":
                        continue;

                    default:
                        break;

                }

                this.addTileEntity( tileEntity );
            }

            this.tileEntityHolders = null;
        }

        this.calculateHeightmap( this.maxHeight );
    }
    // CHECKSTYLE:ON

    /**
     * Loads a chunk section from its raw NBT data.
     *
     * @param section The section to be loaded
     */
    private void loadSection( int sectionY, NBTTagCompound section ) {
        byte[] blocks = section.getByteArray( "Blocks", new byte[0] );
        byte[] addBlocks = section.getByteArray( "Add", new byte[0] );

        NibbleArray add = addBlocks.length > 0 ? new NibbleArray( addBlocks ) : null;
        NibbleArray data = new NibbleArray( section.getByteArray( "Data", new byte[0] ) );

        if ( blocks == null ) {
            throw new IllegalArgumentException( "Corrupt chunk: Section is missing obligatory compounds" );
        }

        for ( int j = 0; j < 16; ++j ) {
            for ( int i = 0; i < 16; ++i ) {
                for ( int k = 0; k < 16; ++k ) {
                    int y = sectionY + j;
                    short blockIndex = (short) ( j << 8 | k << 4 | i );

                    byte blockId = (byte) ( ( ( add != null ? add.get( blockIndex ) << 8 : 0 ) | blocks[blockIndex] ) & 0xFF );
                    byte blockData = data.get( blockIndex );

                    if ( !this.converted ) {
                        Pair<Byte, Byte> convertedData = CONVERTER.convert( blockId, blockData );
                        if ( convertedData != null ) {
                            blockId = convertedData.getFirst();
                            blockData = convertedData.getSecond();
                        }

                        // Block data converter
                        if ( blockId == 3 && blockData == 1 ) {
                            blockId = (byte) 198;
                            blockData = 0;
                        } else if ( blockId == 3 && blockData == 2 ) {
                            blockId = (byte) 243;
                            blockData = 0;
                        }

                        // Fix water & lava at the bottom of a chunk
                        if ( y == 0 && ( blockId == 8 || blockId == 9 || blockId == 10 || blockId == 11 ) ) {
                            blockId = 7;
                            blockData = 0;
                        }
                    }

                    this.setBlock( i, y, k, blockId );

                    if ( blockData != 0 ) {
                        this.setData( i, y, k, blockData );
                    }

                    switch ( blockId ) {
                        case 29:
                        case 33: // Piston head
                            BlockPosition position = new BlockPosition( ( this.x << 4 ) + i, y, ( this.z << 4 ) + k );
                            this.postProcessors.offer( new PistonPostProcessor( this.world, position ) );
                            break;

                        default:
                            break;
                    }
                }
            }
        }
    }

}
