package com.github.nnnnusui.minecraft.nem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class ResourceGenerator {
    private static Path resourcePath   = Paths.get("resourcepacks", "NotEnoughMaterials");
    private static Path stateJsonPath  = Paths.get(resourcePath.toString(), "assets", NotEnoughMaterials.modId, "blockstates", MaterialBlock.registryName +".json");
    private static Path packMcmetaPath = Paths.get(resourcePath.toString(), "pack.mcmeta");
    private static Pattern statePattern = Pattern.compile("(?<=model=)[^\"]*");

    static Collection<String> getStateList(){
        try {
            generate();
            return Files.lines(stateJsonPath)
                        .map(    line  -> statePattern.matcher(line))
                        .filter( Matcher::find)
                        .map(    Matcher::group)
                        .filter( group -> !group.isEmpty())
                        .collect(Collectors.toList());
        } catch (IOException exception) { return Collections.emptyList(); }
    }
    private static void generate() throws IOException{
        if (!Files.exists(resourcePath))   Files.createDirectories(resourcePath);
        if (!Files.exists(stateJsonPath))  generateStateJson();
        if (!Files.exists(packMcmetaPath)) generatePackMcmeta();
    }
    private static void generateStateJson() throws IOException{
        Files.createDirectories(stateJsonPath.getParent());
        Files.createFile(stateJsonPath);
        Files.write(
                stateJsonPath
                , Arrays.asList(
                        "{"
                        ,"  \"variants\": {"
                        ,"    \""+ MaterialBlock.propertyName +"=nem\": { \"model\": \"nem:block/material\" },"
                        ,"    \""+ MaterialBlock.propertyName +"=example\": { \"model\": \"block/stone\" }"
                        ,"  }"
                        ,"}"
                )
                , StandardOpenOption.WRITE
        );
    }
    private static void generatePackMcmeta() throws IOException{
        Files.createFile(packMcmetaPath);
        Files.write(
                packMcmetaPath
                ,Arrays.asList(
                        "{"
                        ,"    \"pack\": {"
                        ,"        \"description\": \"NotEnoughMaterials generated resourcepack.\","
                        ,"        \"pack_format\": 5,"
                        ,"        \"_comment\": \"\""
                        ,"    }"
                        ,"}"
                )
                ,StandardOpenOption.WRITE
        );
    }
}
