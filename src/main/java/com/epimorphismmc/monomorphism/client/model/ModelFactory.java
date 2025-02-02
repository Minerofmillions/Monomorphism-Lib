package com.epimorphismmc.monomorphism.client.model;

import com.epimorphismmc.monomorphism.utility.RegisteredObjects;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

import static com.epimorphismmc.monomorphism.client.utils.ClientUtils.blockRendererDispatcher;
import static com.epimorphismmc.monomorphism.client.utils.ClientUtils.mc;

public class ModelFactory {

    public static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();

    private static TextureAtlasSprite missingSprite;

    /**
     * Fetches Minecraft's Model Manager
     *
     * @return the ModelManager object
     */
    public static ModelManager modelManager() {
        return mc().getModelManager();
    }

    /**
     * Fetches Minecraft's ModelBakery
     *
     * @return the ModelBakery object
     */
    public static ModelBakery modeBakery() {
        return modelManager().getModelBakery();
    }

    // TODO
    public static ModelBaker getModeBaker() {
        return null;
    }

    public static UnbakedModel getUnbakedModel(ResourceLocation modelLocation) {
        return modeBakery().getModel(modelLocation);
    }

    public static BakedModel getBakedModel(ResourceLocation resourceLocation) {
        return modelManager().getModel(resourceLocation);
    }

    /**
     * Fetches the IBakedModel for a BlockState
     *
     * @param state the BlockState
     * @return the IBakedModel
     */
    public static BakedModel getModelForState(BlockState state) {
        return blockRendererDispatcher().getBlockModel(state);
    }

    public static List<ModelResourceLocation> getAllBlockStateModelLocations(Block block) {
        List<ModelResourceLocation> models = new ArrayList<>();
        ResourceLocation blockRl = RegisteredObjects.getKeyOrThrow(block);
        block
                .getStateDefinition()
                .getPossibleStates()
                .forEach(state -> models.add(BlockModelShaper.stateToModelLocation(blockRl, state)));
        return models;
    }

    public static ModelResourceLocation getItemModelLocation(Item item) {
        return new ModelResourceLocation(RegisteredObjects.getKeyOrThrow(item), "inventory");
    }

    public static TextureAtlasSprite getBlockSprite(ResourceLocation location) {
        return mc().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(location);
    }

    public static TextureAtlasSprite getSprite(ResourceLocation atlas, ResourceLocation location) {
        return mc().getTextureAtlas(atlas).apply(location);
    }

    /**
     * Fetches the AtlasTexture object representing the Texture Atlas
     *
     * @param location the location for the atlas
     * @return the AtlasTexture object
     */
    public static TextureAtlas getTextureAtlas(ResourceLocation location) {
        return modelManager().getAtlas(location);
    }

    /**
     * Fetches the sprite on a Texture Atlas related to a render material
     *
     * @param material the render material
     * @return the sprite
     */
    public static TextureAtlasSprite getSprite(Material material) {
        return getTextureAtlas(material.atlasLocation()).getSprite(material.texture());
    }

    /**
     * Converts a String to a RenderMaterial for the Block Atlas
     * @param string the String
     * @return the RenderMaterial
     */
    public static Material getRenderMaterial(String string) {
        return getRenderMaterial(new ResourceLocation(string));
    }

    /**
     * Converts a ResourceLocation to a RenderMaterial for the Block Atlas
     * @param texture the ResourceLocation
     * @return the RenderMaterial
     */
    public static Material getRenderMaterial(ResourceLocation texture) {
        return new Material(InventoryMenu.BLOCK_ATLAS, texture);
    }

    /**
     * @return the TextureAtlasSprite for the missing texture
     */
    public static TextureAtlasSprite getMissingSprite() {
        if (missingSprite == null) {
            missingSprite = getBlockSprite(MissingTextureAtlasSprite.getLocation());
        }
        return missingSprite;
    }

    public static BakedModel getMissingModel() {
        return modelManager().getMissingModel();
    }

    public static Quaternionf getQuaternion(Direction facing) {
        return switch (facing) {
            case UP -> new Quaternionf().rotateXYZ(Mth.HALF_PI, 0, 0);
            case DOWN -> new Quaternionf().rotateXYZ(-Mth.HALF_PI, 0, 0);
            case EAST -> new Quaternionf().rotateXYZ(0, -Mth.HALF_PI, 0);
            case WEST -> new Quaternionf().rotateXYZ(0, Mth.HALF_PI, 0);
            case SOUTH -> new Quaternionf().rotateXYZ(0, Mth.PI, 0);
            case NORTH -> new Quaternionf();
        };
    }

    public static ModelState getRotation(Direction facing) {
        return switch (facing) {
            case DOWN -> BlockModelRotation.X90_Y0;
            case UP -> BlockModelRotation.X270_Y0;
            case NORTH -> BlockModelRotation.X0_Y0;
            case SOUTH -> BlockModelRotation.X0_Y180;
            case WEST -> BlockModelRotation.X0_Y270;
            case EAST -> BlockModelRotation.X0_Y90;
        };
    }

    public static Direction modelFacing(Direction side, Direction frontFacing) {
        if (side == frontFacing) return Direction.NORTH;
        if (frontFacing == Direction.NORTH) return side;
        if (frontFacing == Direction.SOUTH) {
            if (side.getAxis() == Direction.Axis.Y) return side;
            return side.getOpposite();
        }
        if (frontFacing == Direction.EAST) {
            if (side.getAxis() == Direction.Axis.Y) return side;
            return side.getCounterClockWise();
        }
        if (frontFacing == Direction.WEST) {
            if (side.getAxis() == Direction.Axis.Y) return side;
            return side.getClockWise();
        }
        if (frontFacing == Direction.UP) {
            if (side == Direction.DOWN) return Direction.SOUTH;
            if (side.getAxis() == Direction.Axis.X) return side;
            if (side == Direction.SOUTH) return Direction.UP;
            if (side == Direction.NORTH) return Direction.DOWN;
        }
        if (frontFacing == Direction.DOWN) {
            if (side == Direction.UP) return Direction.SOUTH;
            if (side.getAxis() == Direction.Axis.X) return side;
            if (side == Direction.SOUTH) return Direction.DOWN;
            if (side == Direction.NORTH) return Direction.UP;
        }
        return side;
    }
}
