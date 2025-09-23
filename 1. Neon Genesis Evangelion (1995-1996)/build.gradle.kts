import myaa.subkt.ass.*
import myaa.subkt.tasks.*
import myaa.subkt.tasks.Mux.*
import myaa.subkt.tasks.Nyaa.*
import java.awt.Color
import java.time.*

plugins {
    id("myaa.subkt")
}

subs {
    readProperties("sub.properties")
    episodes(getList("episodes"))

    merge {
        from(get("dialogue")) {
            incrementLayer(99)
        }
        
        if (propertyExists("OP_name")) {
            from(get("OP")) {
                syncSourceLine("sync")
                syncTargetLine("opsync")
            }
        }

        if (propertyExists("ED_name")) {
            from(get("ED")) {
                syncSourceLine("sync")
                syncTargetLine("edsync")
            }
        }

        fromIfPresent(get("extra"), ignoreMissingFiles = true)
        fromIfPresent(getList("TS"), ignoreMissingFiles = true)
        fromIfPresent(getList("INS"), ignoreMissingFiles = true)

        includeExtraData(false)
        includeProjectGarbage(false)

        scriptInfo {
            title = get("group_full").get()
            scaledBorderAndShadow = true
        }
    }

    chapters {
        from(merge.item())
        chapterMarker("chptr")
    }

    mux {
        title(get("title"))

        from(get("raw")) {
            tracks {
                include(track.type == TrackType.VIDEO || track.type == TrackType.AUDIO)
            }
            
            video {
                name(get("vtrack"))
                lang("jpn")
                default(true)
            }
            audio(0) {
                name(get("atrack"))
                lang("jpn")
                default(true)
            }

            includeChapters(false)
            subtitles {
                include(false)
            }
            attachments {
                include(false)
            }
        }

        from(merge.item()) {
            tracks {
                name(get("group_full"))
                lang("vie")
                default(true)
                forced(false)
                compression(CompressionType.ZLIB)
            }
        }

        chapters(chapters.item()) { lang("vie") }

        attach(get("fonts")) {
            includeExtensions("ttf", "otf", "ttc")
        }
        verifyFonts(true)
        skipUnusedFonts(true)
        onMissingGlyphs(ErrorMode.WARN)
        onFaux(ErrorMode.WARN)
        
        out(get("muxout"))
    }

    tasks(getList("ncs")) {
        merge {
            from(get("ncsubs"))

            includeExtraData(false)
            includeProjectGarbage(false)

            scriptInfo {
                title = get("group_full").get()
                scaledBorderAndShadow = true
            }
        }

        chapters {
            from(merge.item())
            chapterMarker("ncchapter")
        }

        mux {
            title(get("title"))

            from(get("ncraw")) {
                tracks {
                    include(track.type == TrackType.VIDEO || track.type == TrackType.AUDIO)
                }

                video {
                    lang("jpn")
                    name(get("vtrack"))
                    default(true)
                }
                audio(0) {
                    lang("jpn")
                    name(get("atrack"))
                    default(true)
                }
                
                includeChapters(false)
                subtitles {
                    include(false)
                }
                attachments {
                    include(false)
                }
            }

            from(merge.item()) {
                tracks {
                    name(get("group_full"))
                    lang("vie")
                    default(true)
                    forced(false)
                    compression(CompressionType.ZLIB)
                }
            }

            chapters(chapters.item()) { lang("vie") }

            attach(get("fonts")) {
                includeExtensions("ttf", "otf", "ttc")
            }
            verifyFonts(true)
            skipUnusedFonts(true)
            onMissingGlyphs(ErrorMode.WARN)
            onFaux(ErrorMode.WARN)
            out(get("ncmuxout"))
        }
    }
}
