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
            default: return null;
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
            new DialogueLine("อาคาริ", "\"ฮึ่มมม...! อีกนิดเดียว... อีกแค่ 7 วันก็จะถึงงานเทศกาลแล้วแท้ๆ ทำไมกล่องพวกนี้มันหนักเหมือนใส่หินไว้ข้างในเลยเนี่ย!\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png", ""),
            new DialogueLine("พระเอก", "\"บ่นเป็นคนแก่เลยนะอาคาริ ให้คนแรงเยอะแบบผมจัดการเองดีกว่า เธอไปพักดื่มน้ำก่อนเถอะ\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png", ""),
            new DialogueLine("อาคาริ", "\"อย่าบอกนะว่านายกะจะอู้งานน่ะ! ฉันลงชื่อพวกเราช่วยแต่งเวทีหลักไปแล้วนะ งานนี้ต้องใช้แรงเยอะแน่!\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png", "")
        };
    }
    public static String[] getAkariDay1Choice() { return new String[] {"รับทราบครับกัปตัน! แต่อย่ามาร้องไห้โยเยตอนเห็นฉันยกของหนักกว่าเธอก็แล้วกัน", "โหย ฟังดูเหนื่อยชะมัด แอบไปงีบที่ห้องสมุดไม่ได้เหรอ?"}; }
    public static DialogueLine[] getAkariDay1ResponseA() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"(หน้าแดงวูบหนึ่งแล้วยิ้มกว้าง) \"เหะ! ฝันไปเถอะย่ะ! ใครแพ้ต้องเลี้ยงไอศกรีมหลังซ้อมพรุ่งนี้นะ ตกลงมั้ย?\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png", ""),
            new DialogueLine("พระเอก", "\"ได้เลย เตรียมกระเป๋าฉีกไว้ได้เลยนะกัปตัน เพราะผมไม่มีทางแพ้หรอก\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png", ""),
            new DialogueLine("อาคาริ", "\"มั่นใจจังนะ! แต่ก็... ขอบใจนะที่มาช่วย ความจริงฉันเริ่มจะปวดแขนอยู่พอดีเลยล่ะ\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png", "")
        };
    }
    public static DialogueLine[] getAkariDay1ResponseB() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"นายนี่มันทำตัวเป็นตาแก่ไปได้! งั้นก็เชิญขี้เกียจไปคนเดียวเลยนะ แต่อย่ามาบ่นทีหลังนะถ้าพลาดเรื่องสนุกๆ น่ะ\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png", ""),
            new DialogueLine("พระเอก", "\"เอาน่า ผมแค่ล้อเล่นเอง เดี๋ยวผมช่วยยกกล่องนี้ให้ก็ได้\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png", ""),
            new DialogueLine("อาคาริ", "ไม่ต้องเลย! ฉันไม่อยากพึ่งพาคนไม่มีใจทำงานหรอก ไปนอนให้สบายเลยไป!\" (อาคาริเดินกระแทกส้นเท้าหนีไป)", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png", "")
        };
    }

    // DAY 2
    public static String getAkariDay2Backgroud() { return "image\\Place\\_convenience_store_2.jpg"; }
    public static DialogueLine[] getAkariDay2story() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"อื้มมม! รสช็อกโกแลตมินต์นี่มันที่สุดจริงๆ! ขอบใจนะที่มาช่วยงานเมื่อวาน นายเนี่ย... บางทีก็พึ่งพาได้เหมือนกันนะ\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png", ""),
            new DialogueLine("พระเอก", "\"แค่บางทีเหรอ? ผมนึกว่าผมเป็นฮีโร่ในสายตาเธอไปแล้วซะอีก\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png", ""),
            new DialogueLine("อาคาริ", "\"อย่าหลงตัวเองไปหน่อยเลยน่า! ฉันแค่... ไม่อยากนั่งกินคนเดียวเฉยๆ หรอก! มันเหงานะรู้ไหม\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png", "")
        };
    }
    public static String[] getAkariDay2Choice() { return new String[] {"งั้นผมจะยอมเป็นคนแก้เหงาให้เธอไปตลอดเลยดีมั้ย?", "กินเลอะหมดแล้วนะอาคาริ มานี่เดี๋ยวเช็ดให้"}; }
    public static DialogueLine[] getAkariDay2ResponseA() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"(สำลักไอศกรีม หน้าแดงก่ำ) \"พะ-พูดอะไรน่ะ! ตลอดเลยเหรอ? ตาลุงนี่... ชอบทำตัวเป็นพระเอกมังงะอยู่เรื่อยเลยนะ!\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png", ""),
            new DialogueLine("พระเอก", "\"ก็ถ้ามันทำให้เธอยิ้มได้ ผมเป็นให้ได้ทุกอย่างแหละ\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png", ""),
            new DialogueLine("อาคาริ", "\"บ้า... บ้าที่สุด! พรุ่งนี้ห้ามสายนะ ฉันจะรอที่หน้าโรงเรียน ถ้ามาช้าฉันจะกินไอศกรีมส่วนของนายด้วย!\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png", "")
        };
    }
    public static DialogueLine[] getAkariDay2ResponseB() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"(ปัดมือคุณออกด้วยความอาย) \"หยุดเลยนะ! ฉันไม่ใช่เด็กๆ ซะหน่อย ทำแบบนี้คนอื่นมองมันดูแปลกๆ นะ!\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png", ""),
            new DialogueLine("พระเอก", "\"ก็ผมเห็นมันเลอะจริงๆ นี่นา ไม่ได้คิดอะไรไม่ดีเลย\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png", ""),
            new DialogueLine("อาคาริ", "\"นั่นแหละที่น่ารำคาญ! นายทำเหมือนฉันเป็นเด็กตลอดเลย... วันนี้ฉันกลับเองละกัน เจอกันพรุ่งนี้\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png", "")
        };
    }

    // DAY 3
    public static String getAkariDay3Backgroud() { return "image\\Place\\_school_rooftop_2.jpg"; }
    public static DialogueLine[] getAkariDay3story() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"นายรู้ไหม... ทุกคนมองว่าฉันร่าเริง แต่ความจริงฉันกลัวมากเลย... ฉันเป็นความหวังของชมรม ถ้าวันวิ่งจริงฉันเกิดล้มขึ้นมาล่ะ?\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png", ""),
            new DialogueLine("พระเอก", "\"อาคาริที่มั่นใจหายไปไหนแล้วเนี่ย? ต่อให้เธอจะล้ม ผมก็จะรอรับเธอที่เส้นชัยเอง เชื่อใจผมสิ\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png", "")
        };
    }
    public static String[] getAkariDay3Choice() { return new String[] {"ไม่ต้องสมบูรณ์แบบก็ได้ ขอแค่เป็นอาคาริที่วิ่งอย่างสนุกก็พอแล้ว", "ถ้าเธอล้ม ฉันจะยอมแต่งชุดมาสคอตไปวิ่งเป็นเพื่อนเธอเอง"}; }
    public static DialogueLine[] getAkariDay3ResponseA() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"(นิ่งไปครู่หนึ่งก่อนจะเงยหน้าสบตาคุณ) \"ขอบคุณนะที่บอกแบบนั้น... ปกติมีแต่คนบอกว่าต้องที่หนึ่งเท่านั้น จนฉันลืมไปเลยว่าความสนุกมันเป็นยังไง\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png", ""),
            new DialogueLine("พระเอก", "\"งั้นพรุ่งนี้เรามาซ้อมแบบไม่เน้นความเร็ว แต่เน้นความสนุกกันดีกว่าไหม?\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png", ""),
            new DialogueLine("อาคาริ", "\"อื้ม! ตกลง! ฉันจะวิ่งให้เต็มที่ในแบบของฉันเลยล่ะ!\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png", "")
        };
    }
    public static DialogueLine[] getAkariDay3ResponseB() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"(หลุดขำแต่ดูเหมือนฝืนยิ้ม) \"นายเนี่ยนะ? ชุดมาสคอต? มันจะช่วยให้ฉันหายเครียดหรือจะทำให้ฉันอายคนทั้งโรงเรียนมากกว่ากันนะ\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png", ""),
            new DialogueLine("พระเอก", "\"อ้าว ผมกะจะสร้างสีสันให้เธอเลยนะเนี่ย\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png", ""),
            new DialogueLine("อาคาริ", "\"บางครั้งฉันก็ต้องการความจริงจังนะ... ไม่ใช่แค่มุกตลกไปวันๆ น่ะ แต่ก็ขอบใจนะที่พยายาม\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png", "")
        };
    }

    // DAY 4
    public static String getAkariDay4Backgroud() { return "image\\Place\\_school_in_spring_2.jpg"; }
    public static DialogueLine[] getAkariDay4story() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"ขอบคุณนะที่อยู่เป็นเพื่อนจนป่านนี้... เวลาที่มีนายอยู่ด้วย ฉันรู้สึกว่าฉันวิ่งได้เร็วกว่าปกติอีกแฮะ นาย... เป็นเหมือน 'ถังออกซิเจน' ของฉันเลยล่ะมั้ง?\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png", ""),
            new DialogueLine("พระเอก", "\"เปรียบเทียบซะเสียภาพลักษณ์หมดเลยนะ แต่ก็นะ ผมยินดีเป็นให้ทุกอย่างที่เธอต้องการนั่นแหละ\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png", "")
        };
    }
    public static String[] getAkariDay4Choice() { return new String[] {"ถ้างั้นขอจองตำแหน่งถังออกซิเจนนี้ยาวๆ เลยได้มั้ย?", "งั้นวันงานต้องจ่ายค่าออกซิเจนเป็นรอยยิ้มสวยๆ ด้วยนะ"}; }
    public static DialogueLine[] getAkariDay4ResponseA() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"(กอดแขนคุณเบาๆ) \"พูดแล้วนะ! ห้ามไปเป็นถังออกซิเจนให้คนอื่นล่ะ เพราะฉันต้องการนายคนเดียว... เข้าใจมั้ย?\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png", ""),
            new DialogueLine("พระเอก", "\"เข้าใจครับกัปตัน ผมเป็นถังส่วนตัวของเธอคนเดียวแน่นอน\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png",""),
            new DialogueLine("อาคาริ", "\"ดีมาก! งั้นพรุ่งนี้เดินไปส่งฉันที่บ้านด้วยนะ ฉันมีอะไรจะบอก... แต่ขอเก็บไว้ก่อน\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png","")
        };
    }
    public static DialogueLine[] getAkariDay4ResponseB() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"(ยิ้มฝืนๆ) \"ได้อยู่แล้วล่ะ เรื่องยิ้มฉันถนัดที่สุดนี่นา นายต้องการแค่นั้นจริงๆ เหรอ?\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png",""),
            new DialogueLine("พระเอก", "\"ก็นั่นคือสิ่งที่ดีที่สุดในตัวเธอนี่นา ผมอยากเห็นมันตลอดไปเลย\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png",""),
            new DialogueLine("อาคาริ", "\"อื้ม... นั่นสินะ ฉันก็คงมีดีแค่รอยยิ้มนี่แหละ กลับบ้านเถอะ พรุ่งนี้เจอกัน\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png","")
        };
    }

    // DAY 5
    public static String getAkariDay5Backgroud() { return "image\\Place\\_school_ground_1.jpg"; }
    public static DialogueLine[] getAkariDay5story() {
        return new DialogueLine[] {
            new DialogueLine("เคนจิ", "\"ไงจ๊ะ อาคาริ? ซ้อมไปก็เหนื่อยเปล่า มาเป็น 'ควีน' ของฉันในงานเทศกาลดีกว่าน่า พ่อฉันเป็นสปอนเซอร์รายใหญ่ของที่นี่นะ!\"", "image\\Kenji\\6cbe7b3b-9479-42fd-8b98-86dfdc6e7ae3.png",""),
            new DialogueLine("อาคาริ", "\"ฉันบอกแล้วไงว่าไม่! เลิกใช้เงินของแกมาขู่ฉันสักที!\"", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png","")
        };
    }
    public static String[] getAkariDay5Choice() { return new String[] {"(ก้าวมาขวาง) \"ถอยไปเถอะรุ่นพี่ อาคาริเขาไม่ได้เลือกพี่ และพี่ก็ไม่มีสิทธิ์มาสั่งเธอ!\"", "เราไปจากตรงนี้กันเถอะอาคาริ อย่าไปแลกกับคนแบบนี้เลย"}; }
    public static DialogueLine[] getAkariDay5ResponseA() {
        return new DialogueLine[] {
            new DialogueLine("เคนจิ", "\"หึ! ฝากไว้ก่อนเถอะไอ้หน้าอ่อน พรุ่งนี้ฉันจะทำให้แกต้องเสียใจ!\"", "image\\Kenji\\6cbe7b3b-9479-42fd-8b98-86dfdc6e7ae3.png",""),
            new DialogueLine("พระเอก", "\"เป็นอะไรไหมอาคาริ? ผมขอโทษที่มาช้านะ\"", "image\\Kenji\\6cbe7b3b-9479-42fd-8b98-86dfdc6e7ae3.png",""),
            new DialogueLine("อาคาริ", "\"(โผเข้ากอดคุณทั้งน้ำตา) \"ฮึก... ขอบคุณนะ... ฉันกลัวมากเลย แต่พอนายก้าวออกมาปกป้องฉัน ฉันก็รู้สึกว่าฉันไม่กลัวอะไรอีกแล้ว\"", "image\\Akari\\c4a1d014-6e7f-4c79-b295-13a803c1a711.png","")
        };
    }
    public static DialogueLine[] getAkariDay5ResponseB() {
        return new DialogueLine[] {
            new DialogueLine("เคนจิ", "\"ดูสิอาคาริ! ไอ้ขี้ขลาดนี่มันพาเธอหนีหางจุกตูดเลยว่ะ! ฮ่าๆๆ!\"", "image\\Kenji\\6cbe7b3b-9479-42fd-8b98-86dfdc6e7ae3.png",""),
            new DialogueLine("พระเอก", "\"อาคาริ วิ่งเร็วเข้า เราต้องหนีจากเขา\"", "image\\Kenji\\6cbe7b3b-9479-42fd-8b98-86dfdc6e7ae3.png",""),
            new DialogueLine("อาคาริ", "\"(สลัดมือออก) \"พอที! นายเอาแต่พาฉันหนี! เขาดูถูกฉันขนาดนั้นแต่นายกลับไม่กล้าแม้แต่จะพูดอะไรเลยเหรอ? ฉันผิดหวังในตัวนายจริงๆ!\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png","")
        };
    }

    // DAY 6
    public static String getAkariDay6Backgroud() { return "image\\Place\\_cultural_club_room_3.jpg"; }
    public static DialogueLine[] getAkariDay6story() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"ขอบคุณที่อยู่ข้างๆ ฉันมาตลอดนะ... พรุ่งนี้คือวันสุดท้ายแล้วนะ... ไม่ว่าผลจะเป็นยังไง... สัญญาได้ไหมว่าเราจะมาอยู่ตรงนี้ด้วยกันสองคน?\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png","")
        };
    }
    public static String[] getAkariDay6Choice() { return new String[] {"ถ้าชนะ ฉันขอรางวัลเป็น 'คนข้างๆ' ตลอดไปได้มั้ย?", "ฉันจะรอเธอตรงนี้ ไม่ว่าจะเกิดอะไรขึ้นก็ตาม"}; }
    public static DialogueLine[] getAkariDay6ResponseA() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"(หน้าแดงจัดท่ามกลางความมืด) \"นั่นมัน... คำขอที่เอาแต่ใจที่สุดเลยนะ แต่ก็นะ... ฉันเตรียมคำตอบรอไว้ตั้งนานแล้วล่ะ\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png",""),
            new DialogueLine("พระเอก", "\"แสดงว่าผมมีลุ้นใช่ไหมครับกัปตัน?\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png",""),
            new DialogueLine("อาคาริ", "\"ลุ้นอะไรกันล่ะ นายจองตำแหน่งนั้นไว้ตั้งแต่วันแรกที่มาช่วยฉันยกกล่องแล้วล่ะ ตาบ้า!\"", "image\\Akari\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png","")
        };
    }
    public static DialogueLine[] getAkariDay6ResponseB() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "\"แค่นั้นเหรอ? นายจะรอแค่เพราะมันเป็นสัญญาเฉยๆ เหรอ?\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png",""),
            new DialogueLine("พระเอก", "\"ก็ผมอยากให้เธอสบายใจที่สุดไง ไม่ต้องกดดันเรื่องความสัมพันธ์ตอนนี้ก็ได้\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png",""),
            new DialogueLine("อาคาริ", "\"นายเนี่ย... สุภาพบุรุษเกินไปจนน่าโมโหจริงๆ เลยนะ แต่เอาเถอะ ไว้คุยกันพรุ่งนี้หลังแข่งจบนะ\"", "image\\Akari\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png","")
        };
    }

    public static DialogueLine[] getAkariDateStory() {
    return new DialogueLine[] {
        new DialogueLine("อาคาริ", "ว้าว! สวนสนุกตอนเย็นนี่สวยกว่าที่คิดอีกนะเนี่ย! ขอบใจนะที่พามา... ฉันไม่เคยมาเดินเล่นที่นี่สองคนกับใครเลย", "image\\Akari\\_blank.png",""),
        new DialogueLine("พระเอก", "ผมก็ดีใจนะที่อาคาริจังมีความสุข เราไปกินไอศกรีมทางนั้นกันไหม?\" (เชื่อมโยงสเปกที่เธอชอบไอศกรีม)","image\\Akari\\_blank.png",""),
        new DialogueLine("อาคาริ", "อื้ม! สัญญาแล้วนะ ห้ามผิดคำพูดล่ะ! วันนี้ฉันมีความสุขที่สุดเลย!\"", "image\\Akari\\_blank.png","")
        };
    }

    public static DialogueLine[] getAkariGiftStory() {
    return new DialogueLine[] {
        new DialogueLine("อาคาริ", "เอ๊ะ? ให้ฉันเหรอ? ว้าว! ขอบใจนะ! รู้ใจสมเป็นเพื่อนกันมานานจริงๆ","image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png","")
    };
}


}