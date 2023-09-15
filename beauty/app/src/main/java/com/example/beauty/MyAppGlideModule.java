package com.example.beauty;

import com.bumptech.glide.annotation.Excludes;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.module.LibraryGlideModule;

@GlideModule
@Excludes({LibraryGlideModule.class})
public final class MyAppGlideModule extends AppGlideModule {
    // 在这里配置您的 Glide 模块
}