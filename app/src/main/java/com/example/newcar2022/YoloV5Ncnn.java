// Tencent is pleased to support the open source community by making ncnn available.
//
// Copyright (C) 2020 THL A29 Limited, a Tencent company. All rights reserved.
//
// Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
// in compliance with the License. You may obtain a copy of the License at
//
// https://opensource.org/licenses/BSD-3-Clause
//
// Unless required by applicable law or agreed to in writing, software distributed
// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// CONDITIONS OF ANY KIND, either express or implied. See the License for the
// specific language governing permissions and limitations under the License.

package com.example.newcar2022;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

public class YoloV5Ncnn
{
    public native boolean Init(AssetManager mgr);  //car

    public class Obj
    {
        public float x;
        public float y;
        public float w;
        public float h;
        public String label;
        public float prob;
    }

    public native Obj[] Detect(Bitmap bitmap, boolean use_gpu);

    static {
        System.loadLibrary("yolov5ncnn");
    }


    public native boolean InitS(AssetManager mgr);  //paddleocr

    public class Objs
    {
        public float x0;
        public float y0;
        public float x1;
        public float y1;
        public float x2;
        public float y2;
        public float x3;
        public float y3;
        public String label;
        public float prob;
    }

    public native Objs[] DetectS(Bitmap bitmap, boolean use_gpu);

    static {
        System.loadLibrary("yolov5ncnn");
    }




    public native boolean Initss(AssetManager mgr);   //jtbz

    public class Objss
    {
        public float x;
        public float y;
        public float w;
        public float h;
        public String label;
        public float prob;
    }

    public native Objss[] Detectss(Bitmap bitmap, boolean use_gpu);

    static {
        System.loadLibrary("yolov5ncnn");
    }

    public native boolean Initsss(AssetManager mgr);

    public class Objsss
    {
        public float x;
        public float y;
        public float w;
        public float h;
        public String label;
        public float prob;
    }

    public native Objsss[] Detectsss(Bitmap bitmap, boolean use_gpu);

    static {
        System.loadLibrary("yolov5ncnn");
    }

    public native boolean Initssss(AssetManager mgr);

    public class Objssss
    {
        public float x;
        public float y;
        public float w;
        public float h;
        public String label;
        public float prob;
    }

    public native Objssss[] Detectssss(Bitmap bitmap, boolean use_gpu);

    static {
        System.loadLibrary("yolov5ncnn");
    }


}
