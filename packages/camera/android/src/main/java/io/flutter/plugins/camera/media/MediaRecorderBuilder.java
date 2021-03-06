// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package io.flutter.plugins.camera.media;

import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import androidx.annotation.NonNull;
import java.io.IOException;

import io.flutter.plugins.camera.Camera;

public class MediaRecorderBuilder {
  static class MediaRecorderFactory {
    MediaRecorder makeMediaRecorder() {
      return new MediaRecorder();
    }
  }

  private final String outputFilePath;
  private  final String preset;
  private final CamcorderProfile recordingProfile;
  private final MediaRecorderFactory recorderFactory;

  private boolean enableAudio;
  private int mediaOrientation;

  public MediaRecorderBuilder(
          @NonNull CamcorderProfile recordingProfile, @NonNull String outputFilePath, @NonNull String preset) {
    this(recordingProfile, outputFilePath,preset, new MediaRecorderFactory());
  }

  MediaRecorderBuilder(
      @NonNull CamcorderProfile recordingProfile,
      @NonNull String outputFilePath,
      @NonNull String preset,
      MediaRecorderFactory helper) {
    this.outputFilePath = outputFilePath;
    this.recordingProfile = recordingProfile;
    this.preset = preset;
    this.recorderFactory = helper;
  }

  public MediaRecorderBuilder setEnableAudio(boolean enableAudio) {
    this.enableAudio = enableAudio;
    return this;
  }

  public MediaRecorderBuilder setMediaOrientation(int orientation) {
    this.mediaOrientation = orientation;
    return this;
  }


  public MediaRecorder build() throws IOException {
    MediaRecorder mediaRecorder = recorderFactory.makeMediaRecorder();

    // There's a specific order that mediaRecorder expects. Do not change the order
    // of these function calls.
    if (enableAudio) {
      mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      mediaRecorder.setAudioEncodingBitRate(recordingProfile.audioBitRate);
    }


    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
    mediaRecorder.setOutputFormat(recordingProfile.fileFormat);
    if (enableAudio) mediaRecorder.setAudioEncoder(recordingProfile.audioCodec);
    mediaRecorder.setVideoEncoder(recordingProfile.videoCodec);
    mediaRecorder.setVideoEncodingBitRate(recordingProfile.videoBitRate);
    if (enableAudio) mediaRecorder.setAudioSamplingRate(recordingProfile.audioSampleRate);
    mediaRecorder.setVideoFrameRate(recordingProfile.videoFrameRate);
    if(preset.equals("ultraslowmo"))
    {
      mediaRecorder.setCaptureRate(recordingProfile.videoFrameRate / 0.25f);
    }
    else if(preset.equals("slowmo"))
    {
      mediaRecorder.setCaptureRate(recordingProfile.videoFrameRate / 0.50f);
    }
    else if(preset.equals("timelapse"))
    {
      mediaRecorder.setCaptureRate(recordingProfile.videoFrameRate / 3.0f);
    }
    else if(preset.equals("ultratimelapse"))
    {
      mediaRecorder.setCaptureRate(recordingProfile.videoFrameRate / 6.0f);
    }
    else{
      mediaRecorder.setCaptureRate(recordingProfile.videoFrameRate);
    }
    mediaRecorder.setVideoSize(recordingProfile.videoFrameWidth, recordingProfile.videoFrameHeight);
    mediaRecorder.setOutputFile(outputFilePath);
    mediaRecorder.setOrientationHint(this.mediaOrientation);

    mediaRecorder.prepare();

    return mediaRecorder;
  }
}
