﻿// 批量导出指定路径下的所有组，并根据原画板大小和缩小后的画板大小分别导出

function main() {
    // 检查是否有打开的文档
    if (app.documents.length === 0) {
        alert("没有打开的文档。");
        return;
    }

    var doc = app.activeDocument;
    var outputPath = Folder.selectDialog("选择导出文件夹");
    if (outputPath == null) {
        alert("未选择输出文件夹。");
        return;
    }
    var folderName = outputPath.name.toLowerCase();

    // Check if the folder name ends with "signature"
    var sign = folderName.substr(folderName.length - 9) === "signature";

    // 复制文档
    var copiedDoc = doc.duplicate();

    app.activeDocument = doc;

    if (sign) {
        process(doc, outputPath, "_s_p");
        app.activeDocument = copiedDoc;
        copiedDoc.resizeImage(UnitValue(512, "px"), UnitValue(512, "px"), null, ResampleMethod.BICUBICSHARPER);
        process(copiedDoc, outputPath, "_s");
    } else {
        process(doc, outputPath, "_p");
        app.activeDocument = copiedDoc;
        copiedDoc.resizeImage(UnitValue(250, "px"), UnitValue(190, "px"), null, ResampleMethod.BICUBICSHARPER);
        process(copiedDoc, outputPath, "");
    }

    // 关闭复制文档，不保存更改
    copiedDoc.close(SaveOptions.DONOTSAVECHANGES);

    alert("导出完成！");
}

function process(doc, outputPath, postfix) {
    var visibleLayerSets = getInitiallyVisibleLayerSets(doc);
    if (visibleLayerSets.length === 0) {
        alert("没有可见的图层组。");
        return;
    }

    hideAllGroups(visibleLayerSets);

    var previousVisibleLayerSet = null;

    // 第二次导出，使用复制文档并不添加后缀
    for (var i = 0; i < visibleLayerSets.length; i++) {
        var layerSet = visibleLayerSets[i];

        // 隐藏上一个组（如果存在）
        if (previousVisibleLayerSet !== null) {
            previousVisibleLayerSet.visible = false;
        }
        // 显示当前组
        layerSet.visible = true;

        // 导出当前可见内容，不添加后缀
        saveAsPng(outputPath, layerSet.name + postfix, doc);

        // 记录当前组为上一个可见组
        previousVisibleLayerSet = layerSet;
    }
}

function getInitiallyVisibleLayerSets(doc) {
    var visibleLayerSets = [];
    var layers = doc.layerSets;

    for (var i = 0; i < layers.length; i++) {
        if (layers[i] instanceof LayerSet) {
            for (var j = 0; j < layers[i].layers.length; j++) {
                if (layers[i].layers[j].visible) {
                    visibleLayerSets.push(layers[i].layers[j]);
                }
            }
        }
    }
    return visibleLayerSets;
}

// 隐藏所有图层组
function hideAllGroups(layerSets) {
    for (var i = 0; i < layerSets.length; i++) {
        layerSets[i].visible = false;
    }
}

// 将当前文档保存为PNG格式
function saveAsPng(outputPath, fileName, currentDoc) {
    var file = new File(outputPath + "/" + fileName + ".png");
    var options = new PNGSaveOptions();
    options.compression = 0; // 压缩等级，0（最大速度）到9（最小文件大小）

    currentDoc.saveAs(file, options, true, Extension.LOWERCASE);
}

// 运行脚本
main();
