public class storyData {

    // --- ระบบตัวกลาง (Mapping Methods) เพื่อให้ StoryManager เรียกใช้ผ่านตัวเลข Day ได้ ---

    public static String getAkariDayBackground(int day) {
        switch (day) {
            case 1: return getAkariDay1Backgroud();
            case 2: return getAkariDay2Backgroud();
            case 3: return getAkariDay3Backgroud();
            case 4: return getAkariDay4Backgroud();
            case 5: return getAkariDay5Backgroud();
            case 6: return getAkariDay6Backgroud();
            default: return "image\\Place\\Naohiro.jpg";
        }
    }

    public static DialogueLine[] getAkariDayStory(int day) {
        switch (day) {
            case 1: return getAkariDay1story();
            case 2: return getAkariDay2story();
            case 3: return getAkariDay3story();
            case 4: return getAkariDay4story();
            case 5: return getAkariDay5story();
            case 6: return getAkariDay6story();
            default: return null;
        }
    }

    public static String[] getAkariDayChoice(int day) {
        switch (day) {
            case 1: return getAkariDay1Choice();
            case 2: return getAkariDay2Choice();
            case 3: return getAkariDay3Choice();
            case 4: return getAkariDay4Choice();
            case 5: return getAkariDay5Choice();
            case 6: return getAkariDay6Choice();
            default: return new String[]{"...", "..."};
        }
    }

    public static DialogueLine[] getAkariDayResponseA(int day) {
        switch (day) {
            case 1: return getAkariDay1ResponseA();
            case 2: return getAkariDay2ResponseA();
            case 3: return getAkariDay3ResponseA();
            case 4: return getAkariDay4ResponseA();
            case 5: return getAkariDay5ResponseA();
            case 6: return getAkariDay6ResponseA();
            default: return null;
        }
    }

    public static DialogueLine[] getAkariDayResponseB(int day) {
        switch (day) {
            case 1: return getAkariDay1ResponseB();
            case 2: return getAkariDay2ResponseB();
            case 3: return getAkariDay3ResponseB();
            case 4: return getAkariDay4ResponseB();
            case 5: return getAkariDay5ResponseB();
            case 6: return getAkariDay6ResponseB();
            default: return null;
        }
    }

    // ========================= เนื้อหาเดิมของคุณ (DAY 1 - 6) =========================

    // DAY 1
    public static String getAkariDay1Backgroud() { return "image\\Place\\Naohiro.jpg"; }
    public static DialogueLine[] getAkariDay1story() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"ฮึ่มมม...! อีกนิดเดียว... อีกแค่ 7 วันก็จะถึงงานเทศกาลแล้วแท้ๆ ทำไมกล่องพวกนี้มันหนักเหมือนใส่หินไว้ข้างในเลยเนี่ย!\"", "image\\Akari\\0aaa6b48-e691-4426-9321-dd7c1d29cc97.png"),
            new DialogueLine("พระเอก", "\"บ่นเป็นคนแก่เลยนะอาคาริ ให้คนแรงเยอะแบบผมจัดการเองดีกว่า เธอไปพักดื่มน้ำก่อนเถอะ\"", "image\\Akari\\0aaa6b48-e691-4426-9321-dd7c1d29cc97.png"),
            new DialogueLine("อาคาริ", "\"อย่าบอกนะว่านายกะจะอู้งานน่ะ! ฉันลงชื่อพวกเราช่วยแต่งเวทีหลักไปแล้วนะ งานนี้ต้องใช้แรงเยอะแน่!\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
        };
    }
    public static String[] getAkariDay1Choice() { return new String[] {"รับทราบครับกัปตัน! แต่อย่ามาร้องไห้โยเยตอนเห็นฉันยกของหนักกว่าเธอก็แล้วกัน", "โหย ฟังดูเหนื่อยชะมัด แอบไปงีบที่ห้องสมุดไม่ได้เหรอ?"}; }
    public static DialogueLine[] getAkariDay1ResponseA() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"(หน้าแดงวูบหนึ่งแล้วยิ้มกว้าง) \"เหะ! ฝันไปเถอะย่ะ! ใครแพ้ต้องเลี้ยงไอศกรีมหลังซ้อมพรุ่งนี้นะ ตกลงมั้ย?\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us.png"),
            new DialogueLine("พระเอก", "\"ได้เลย เตรียมกระเป๋าฉีกไว้ได้เลยนะกัปตัน เพราะผมไม่มีทางแพ้หรอก\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us.png"),
            new DialogueLine("อาคาริ", "\"มั่นใจจังนะ! แต่ก็... ขอบใจนะที่มาช่วย ความจริงฉันเริ่มจะปวดแขนอยู่พอดีเลยล่ะ\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us.png")
        };
    }
    public static DialogueLine[] getAkariDay1ResponseB() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"นายนี่มันทำตัวเป็นตาแก่ไปได้! งั้นก็เชิญขี้เกียจไปคนเดียวเลยนะ\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
            new DialogueLine("อาคาริ", "\"ไม่ต้องเลย! ฉันไม่อยากพึ่งพาคนไม่มีใจทำงานหรอก ไปนอนให้สบายเลยไป!\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
        };
    }

    // DAY 2
    public static String getAkariDay2Backgroud() { return "image\\Place\\_convenience_store_2.jpg"; }
    public static DialogueLine[] getAkariDay2story() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"อื้มมม! รสช็อกโกแลตมินต์นี่มันที่สุดจริงๆ! ขอบใจนะที่มาช่วยงานเมื่อวาน นายเนี่ย... บางทีก็พึ่งพาได้เหมือนกันนะ\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png"),
            new DialogueLine("อาคาริ", "\"อย่าหลงตัวเองไปหน่อยเลยน่า! ฉันแค่... ไม่อยากนั่งกินคนเดียวเฉยๆ หรอก! มันเหงานะรู้ไหม\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png")
        };
    }
    public static String[] getAkariDay2Choice() { return new String[] {"งั้นผมจะยอมเป็นคนแก้เหงาให้เธอไปตลอดเลยดีมั้ย?", "กินเลอะหมดแล้วนะอาคาริ มานี่เดี๋ยวเช็ดให้"}; }
    public static DialogueLine[] getAkariDay2ResponseA() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"(หน้าแดงก่ำ) \"พะ-พูดอะไรน่ะ! ตลอดเลยเหรอ? ตาลุงนี่... ชอบทำตัวเป็นพระเอกมังงะอยู่เรื่อยเลยนะ!\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us.png"),
            new DialogueLine("อาคาริ", "\"แต่วันนี้ฉันกลับเองละกัน เจอกันพรุ่งนี้!\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us.png")
        };
    }
    public static DialogueLine[] getAkariDay2ResponseB() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"หยุดเลยนะ! ฉันไม่ใช่เด็กๆ ซะหน่อย ทำแบบนี้คนอื่นมองมันดูแปลกๆ นะ!\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
        };
    }

    // DAY 3
    public static String getAkariDay3Backgroud() { return "image\\Place\\_school_rooftop_2.jpg"; }
    public static DialogueLine[] getAkariDay3story() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"นายรู้ไหม... ทุกคนมองว่าฉันร่าเริง แต่ความจริงฉันกลัวมากเลย... ถ้าวันวิ่งจริงฉันเกิดล้มขึ้นมาล่ะ?\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png"),
            new DialogueLine("พระเอก", "\"ต่อให้เธอจะล้ม ผมก็จะรอรับเธอที่เส้นชัยเอง เชื่อใจผมสิ\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png")
        };
    }
    public static String[] getAkariDay3Choice() { return new String[] {"ไม่ต้องสมบูรณ์แบบก็ได้ ขอแค่เป็นอาคาริที่วิ่งอย่างสนุกก็พอแล้ว", "ถ้าเธอล้ม ฉันจะยอมแต่งชุดมาสคอตไปวิ่งเป็นเพื่อนเธอเอง"}; }
    public static DialogueLine[] getAkariDay3ResponseA() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"ขอบคุณนะ... ปกติมีแต่คนบอกว่าต้องที่หนึ่งเท่านั้น จนฉันลืมไปเลยว่าความสนุกมันเป็นยังไง\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us.png")
        };
    }
    public static DialogueLine[] getAkariDay3ResponseB() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"ชุดมาสคอต? นายจะทำให้ฉันอายคนทั้งโรงเรียนมากกว่าเดิมล่ะสิ!\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
        };
    }

    // DAY 4
    public static String getAkariDay4Backgroud() { return "image\\Place\\_school_in_spring_2.jpg"; }
    public static DialogueLine[] getAkariDay4story() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"เวลาที่มีนายอยู่ด้วย ฉันรู้สึกว่าฉันวิ่งได้เร็วกว่าปกติอีกแฮะ นาย... เป็นเหมือน 'ถังออกซิเจน' ของฉันเลยล่ะมั้ง?\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png")
        };
    }
    public static String[] getAkariDay4Choice() { return new String[] {"ถ้างั้นขอจองตำแหน่งถังออกซิเจนนี้ยาวๆ เลยได้มั้ย?", "งั้นวันงานต้องจ่ายค่าออกซิเจนเป็นรอยยิ้มสวยๆ ด้วยนะ"}; }
    public static DialogueLine[] getAkariDay4ResponseA() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"(กอดแขนคุณเบาๆ) \"พูดแล้วนะ! ห้ามไปเป็นถังออกซิเจนให้คนอื่นล่ะ เพราะฉันต้องการนายคนเดียว!\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us.png")
        };
    }
    public static DialogueLine[] getAkariDay4ResponseB() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"อื้ม... นั่นสินะ ฉันก็คงมีดีแค่รอยยิ้มนี่แหละ กลับบ้านเถอะ\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
        };
    }

    // DAY 5
    public static String getAkariDay5Backgroud() { return "image\\Place\\_school_ground_1.jpg"; }
    public static DialogueLine[] getAkariDay5story() {
        return new DialogueLine[] {
            new DialogueLine("เคนจิ", "\"มาเป็น 'ควีน' ของฉันในงานเทศกาลดีกว่าน่า อาคาริ!\"", "image\\Kenji\\6cbe7b3b-9479-42fd-8b98-86dfdc6e7ae3.png"),
            new DialogueLine("อาคาริ", "\"ฉันบอกแล้วไงว่าไม่! เลิกใช้เงินของแกมาขู่ฉันสักที!\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png")
        };
    }
    public static String[] getAkariDay5Choice() { return new String[] {"ถอยไปเถอะรุ่นพี่ อาคาริเขาไม่ได้เลือกพี่!", "เราไปจากตรงนี้กันเถอะอาคาริ อย่าไปแลกกับคนแบบนี้เลย"}; }
    public static DialogueLine[] getAkariDay5ResponseA() {
        return new DialogueLine[] {
            new DialogueLine("เคนจิ", "\"หึ! ฝากไว้ก่อนเถอะไอ้หน้าอ่อน!\"", "image\\Kenji\\6cbe7b3b-9479-42fd-8b98-86dfdc6e7ae3.png"),
            new DialogueLine("อาคาริ", "\"ฮึก... ขอบคุณนะ... พอมีนายอยู่ ฉันก็ไม่กลัวอะไรอีกแล้ว\"", "image\\Akari\\c4a1d014-6e7f-4c79-b295-13a803c1a711.png")
        };
    }
    public static DialogueLine[] getAkariDay5ResponseB() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"นายเอาแต่พาฉันหนี! ฉันผิดหวังในตัวนายจริงๆ!\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
        };
    }

    // DAY 6
    public static String getAkariDay6Backgroud() { return "image\\Place\\_cultural_club_room_3.jpg"; }
    public static DialogueLine[] getAkariDay6story() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"ขอบคุณที่อยู่ข้างๆ ฉันมาตลอดนะ... สัญญาได้ไหมว่าพรุ่งนี้เราจะมาอยู่ตรงนี้ด้วยกันสองคน?\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us.png")
        };
    }
    public static String[] getAkariDay6Choice() { return new String[] {"ถ้าชนะ ฉันขอรางวัลเป็น 'คนข้างๆ' ตลอดไปได้มั้ย?", "ฉันจะรอเธอตรงนี้ ไม่ว่าจะเกิดอะไรขึ้นก็ตาม"}; }
    public static DialogueLine[] getAkariDay6ResponseA() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"นายจองตำแหน่งนั้นไว้ตั้งแต่วันแรกที่มาช่วยฉันยกกล่องแล้วล่ะ ตาบ้า!\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us.png")
        };
    }
    public static DialogueLine[] getAkariDay6ResponseB() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"นายเนี่ย... สุภาพบุรุษเกินไปจนน่าโมโหจริงๆ เลยนะ\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
        };
    }
}