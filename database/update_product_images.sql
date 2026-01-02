-- =====================================================
-- Update Product Images - Smart E-Commerce System
-- Date: December 24, 2025
-- Purpose: Map existing products to image paths
-- =====================================================

-- =====================================================
-- ACCESSORIES CATEGORY
-- =====================================================

-- Beauty & Makeup Products
UPDATE Products SET image_url = 'images/Accessories/BlushBrush2.jpg'
WHERE product_name LIKE '%Blush Brush%' OR product_name LIKE '%Makeup Brush%';

UPDATE Products SET image_url = 'images/Accessories/Concealer.jpg'
WHERE product_name LIKE '%Concealer%';

UPDATE Products SET image_url = 'images/Accessories/Lipgloss.jpg'
WHERE product_name LIKE '%Lip Gloss%' OR product_name LIKE '%Lipgloss%';

UPDATE Products SET image_url = 'images/Accessories/lipstick.jpg'
WHERE product_name LIKE '%Lipstick%' AND image_url IS NULL;

UPDATE Products SET image_url = 'images/Accessories/NailPolishPackof3.png'
WHERE product_name LIKE '%Nail Polish%';

-- Appliances
UPDATE Products SET image_url = 'images/Accessories/Appliances/ElectricHairBrush.jpg'
WHERE product_name LIKE '%Electric Hair Brush%' OR product_name LIKE '%Hair Brush Electric%';

UPDATE Products SET image_url = 'images/Accessories/Appliances/epilator.jpg'
WHERE product_name LIKE '%Epilator%';

UPDATE Products SET image_url = 'images/Accessories/Appliances/HairDryer.png'
WHERE product_name LIKE '%Hair Dryer%' OR product_name LIKE '%Blow Dryer%';

UPDATE Products SET image_url = 'images/Accessories/Appliances/HairTimmer.png'
WHERE product_name LIKE '%Hair Trimmer%' OR product_name LIKE '%Trimmer%';

UPDATE Products SET image_url = 'images/Accessories/Appliances/Hair_Straigthner.png'
WHERE product_name LIKE '%Hair Straightener%' OR product_name LIKE '%Straightener%';

-- Fragrance
UPDATE Products SET image_url = 'images/Accessories/Fragrance/AQUA_ALGORIA_GODESS.png'
WHERE product_name LIKE '%Aqua%' AND product_name LIKE '%Guerlain%';

UPDATE Products SET image_url = 'images/Accessories/Fragrance/CHANEL_ETHER.png'
WHERE product_name LIKE '%Chanel%' AND product_name LIKE '%Perfume%';

UPDATE Products SET image_url = 'images/Accessories/Fragrance/DAISY_MARKJACOBS.png'
WHERE product_name LIKE '%Daisy%' AND product_name LIKE '%Marc Jacobs%';

UPDATE Products SET image_url = 'images/Accessories/Fragrance/DOLCE_GABANA_THEONE.png'
WHERE product_name LIKE '%Dolce%' AND product_name LIKE '%Gabbana%';

UPDATE Products SET image_url = 'images/Accessories/Fragrance/JEANE_LANVIN_THESIN.png'
WHERE product_name LIKE '%Lanvin%';

-- Hair Care
UPDATE Products SET image_url = 'images/Accessories/HairCare/Bath&BodyWORKS.jpg'
WHERE product_name LIKE '%Bath%Body Works%';

UPDATE Products SET image_url = 'images/Accessories/HairCare/HairConditioner.jpg'
WHERE product_name LIKE '%Hair Conditioner%' OR product_name LIKE '%Conditioner%';

UPDATE Products SET image_url = 'images/Accessories/HairCare/HairCream.png'
WHERE product_name LIKE '%Hair Cream%';

UPDATE Products SET image_url = 'images/Accessories/HairCare/HairSerum.jpg'
WHERE product_name LIKE '%Hair Serum%';

UPDATE Products SET image_url = 'images/Accessories/HairCare/HairSpray.jpg'
WHERE product_name LIKE '%Hair Spray%';

-- Skincare
UPDATE Products SET image_url = 'images/Accessories/Skincare/BodyLotion.jpg'
WHERE product_name LIKE '%Body Lotion%' OR product_name LIKE '%Moisturizer%';

UPDATE Products SET image_url = 'images/Accessories/Skincare/BrownieLipBalm.jpg'
WHERE product_name LIKE '%Lip Balm%';

UPDATE Products SET image_url = 'images/Accessories/Skincare/FaceMask.jpg'
WHERE product_name LIKE '%Face Mask%';

UPDATE Products SET image_url = 'images/Accessories/Skincare/FaceMoisturisers.jpg'
WHERE product_name LIKE '%Face Moisturizer%' OR product_name LIKE '%Face Cream%';

UPDATE Products SET image_url = 'images/Accessories/Skincare/SunScreen.jpg'
WHERE product_name LIKE '%Sunscreen%' OR product_name LIKE '%Sun Screen%' OR product_name LIKE '%SPF%';

-- Smart Wearables
UPDATE Products SET image_url = 'images/Accessories/SmartWearables/Aplle_Nike_Watch.jpg'
WHERE product_name LIKE '%Apple%' AND product_name LIKE '%Nike%' AND product_name LIKE '%Watch%';

UPDATE Products SET image_url = 'images/Accessories/SmartWearables/FitnessNeckBand.jpg'
WHERE product_name LIKE '%Fitness%' AND product_name LIKE '%Neckband%';

UPDATE Products SET image_url = 'images/Accessories/SmartWearables/FrenchConnectionFitness.jpg'
WHERE product_name LIKE '%French Connection%' AND product_name LIKE '%Fitness%';

UPDATE Products SET image_url = 'images/Accessories/SmartWearables/SennheiserBlackHD350.jpg'
WHERE product_name LIKE '%Sennheiser%' OR (product_name LIKE '%Headphone%' AND product_name LIKE '%HD350%');

UPDATE Products SET image_url = 'images/Accessories/SmartWearables/WirelessAirpods.jpg'
WHERE product_name LIKE '%Airpods%' OR product_name LIKE '%Wireless Earbuds%';

-- =====================================================
-- HOME & LIVING CATEGORY
-- =====================================================

-- Chairs
UPDATE Products SET image_url = 'images/HomeNliving/BeachChair.png'
WHERE product_name LIKE '%Beach Chair%';

UPDATE Products SET image_url = 'images/HomeNliving/Funky_ColorBlock.png'
WHERE product_name LIKE '%Colorblock%' AND product_name LIKE '%Chair%';

UPDATE Products SET image_url = 'images/HomeNliving/GreaySuede.png'
WHERE product_name LIKE '%Grey Suede%' AND product_name LIKE '%Chair%';

UPDATE Products SET image_url = 'images/HomeNliving/KitchenBar_Chair.png'
WHERE product_name LIKE '%Kitchen%' OR product_name LIKE '%Bar Chair%';

UPDATE Products SET image_url = 'images/HomeNliving/Leather-LoungeChair.png'
WHERE product_name LIKE '%Leather%' AND product_name LIKE '%Lounge%';

UPDATE Products SET image_url = 'images/HomeNliving/office_Chair.png'
WHERE product_name LIKE '%Office Chair%';

UPDATE Products SET image_url = 'images/HomeNliving/WoodenChairs.png'
WHERE product_name LIKE '%Wooden Chair%';

-- Carpets
UPDATE Products SET image_url = 'images/HomeNliving/Carpet/Abstract.png'
WHERE product_name LIKE '%Abstract%' AND product_name LIKE '%Carpet%';

UPDATE Products SET image_url = 'images/HomeNliving/Carpet/BlackGoldStripes.png'
WHERE product_name LIKE '%Black%Gold%' AND product_name LIKE '%Carpet%';

UPDATE Products SET image_url = 'images/HomeNliving/Carpet/FadedModernBlocks.png'
WHERE product_name LIKE '%Faded%' AND product_name LIKE '%Carpet%';

UPDATE Products SET image_url = 'images/HomeNliving/Carpet/NavyBlueDesign.png'
WHERE product_name LIKE '%Navy Blue%' AND product_name LIKE '%Carpet%';

UPDATE Products SET image_url = 'images/HomeNliving/Carpet/OffWhiteFur.png'
WHERE product_name LIKE '%Off White%' AND product_name LIKE '%Fur%' AND product_name LIKE '%Carpet%';

-- Dining Tables
UPDATE Products SET image_url = 'images/HomeNliving/Dining/8_Seater.png'
WHERE product_name LIKE '%8 Seater%' OR product_name LIKE '%8-Seater%';

UPDATE Products SET image_url = 'images/HomeNliving/Dining/marblewhite6seater.png'
WHERE product_name LIKE '%6 Seater%' AND product_name LIKE '%Marble%';

UPDATE Products SET image_url = 'images/HomeNliving/Dining/RoundTableDarkGreySuede.png'
WHERE product_name LIKE '%Round Table%' AND product_name LIKE '%Grey Suede%';

UPDATE Products SET image_url = 'images/HomeNliving/Dining/Table_2_Wooden.png'
WHERE product_name LIKE '%2 Seater%' AND product_name LIKE '%Wooden%';

UPDATE Products SET image_url = 'images/HomeNliving/Dining/Table_4_PeacheWhite.png'
WHERE product_name LIKE '%4 Seater%' AND product_name LIKE '%Peach%';

-- Lamps
UPDATE Products SET image_url = 'images/HomeNliving/Lamp/CustomNeonLights.png'
WHERE product_name LIKE '%Neon%' AND product_name LIKE '%Light%';

UPDATE Products SET image_url = 'images/HomeNliving/Lamp/KitchenLights.png'
WHERE product_name LIKE '%Kitchen%' AND product_name LIKE '%Light%';

UPDATE Products SET image_url = 'images/HomeNliving/Lamp/LoungeLights.png'
WHERE product_name LIKE '%Lounge%' AND product_name LIKE '%Light%';

UPDATE Products SET image_url = 'images/HomeNliving/Lamp/ModernBedroomLights.png'
WHERE product_name LIKE '%Bedroom%' AND product_name LIKE '%Light%';

UPDATE Products SET image_url = 'images/HomeNliving/Lamp/StudyRoomLight.png'
WHERE product_name LIKE '%Study%' AND product_name LIKE '%Light%';

-- Side Tables
UPDATE Products SET image_url = 'images/HomeNliving/SideTable/CircleTable.png'
WHERE product_name LIKE '%Circle%' AND product_name LIKE '%Table%';

UPDATE Products SET image_url = 'images/HomeNliving/SideTable/LightBrown_MultiDrawertable.png'
WHERE product_name LIKE '%Multi Drawer%' AND product_name LIKE '%Table%';

UPDATE Products SET image_url = 'images/HomeNliving/SideTable/OakWoodenTables.png'
WHERE product_name LIKE '%Oak%' AND product_name LIKE '%Table%';

UPDATE Products SET image_url = 'images/HomeNliving/SideTable/OffwhiteModernSideTable.png'
WHERE product_name LIKE '%Off White%' AND product_name LIKE '%Side Table%';

UPDATE Products SET image_url = 'images/HomeNliving/SideTable/WhiteSmallBedroomTable.png'
WHERE product_name LIKE '%White%' AND product_name LIKE '%Bedroom%' AND product_name LIKE '%Table%';

-- Sofas
UPDATE Products SET image_url = 'images/HomeNliving/Sofas/BlackLeatherLshaped.png'
WHERE product_name LIKE '%Black%' AND product_name LIKE '%Leather%' AND product_name LIKE '%L-Shaped%';

UPDATE Products SET image_url = 'images/HomeNliving/Sofas/BrownLeather.png'
WHERE product_name LIKE '%Brown%' AND product_name LIKE '%Leather%' AND product_name LIKE '%Sofa%';

UPDATE Products SET image_url = 'images/HomeNliving/Sofas/ClassicWhite6Seater.png'
WHERE product_name LIKE '%White%' AND product_name LIKE '%6 Seater%' AND product_name LIKE '%Sofa%';

UPDATE Products SET image_url = 'images/HomeNliving/Sofas/GreyBlue.png'
WHERE product_name LIKE '%Grey Blue%' AND product_name LIKE '%Sofa%';

UPDATE Products SET image_url = 'images/HomeNliving/Sofas/UshapedLightGrey.png'
WHERE product_name LIKE '%U-Shaped%' AND product_name LIKE '%Grey%';

-- =====================================================
-- MEN'S CATEGORY
-- =====================================================

-- Men's Watches
UPDATE Products SET image_url = 'images/image2/Men/AppleWatchseries6.jpg'
WHERE product_name LIKE '%Apple Watch%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Men%');

UPDATE Products SET image_url = 'images/image2/Men/BrownTissot.jpg'
WHERE product_name LIKE '%Tissot%' AND product_name LIKE '%Brown%';

UPDATE Products SET image_url = 'images/image2/Men/FossilClassic.jpg'
WHERE product_name LIKE '%Fossil%' AND product_name LIKE '%Classic%';

UPDATE Products SET image_url = 'images/image2/Men/FOSSILgoldchain.jpg'
WHERE product_name LIKE '%Fossil%' AND product_name LIKE '%Gold%';

UPDATE Products SET image_url = 'images/image2/Men/MVMTrosegolddial.jpg'
WHERE product_name LIKE '%MVMT%' OR product_name LIKE '%Rose Gold%';

UPDATE Products SET image_url = 'images/image2/Men/NauticaBrown.jpg'
WHERE product_name LIKE '%Nautica%';

UPDATE Products SET image_url = 'images/image2/Men/TimexBrownBelt.jpg'
WHERE product_name LIKE '%Timex%';

-- Men's Bottoms
UPDATE Products SET image_url = 'images/image2/Men/Bottoms/CheckGreyChinos.png'
WHERE product_name LIKE '%Check%' AND product_name LIKE '%Grey%' AND product_name LIKE '%Chinos%';

UPDATE Products SET image_url = 'images/image2/Men/Bottoms/greyChinos.png'
WHERE product_name LIKE '%Grey%' AND product_name LIKE '%Chinos%' AND product_name NOT LIKE '%Check%';

UPDATE Products SET image_url = 'images/image2/Men/Bottoms/Jeans.png'
WHERE product_name LIKE '%Jeans%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Men%');

UPDATE Products SET image_url = 'images/image2/Men/Bottoms/NavyBlueFormalTrousers.png'
WHERE product_name LIKE '%Navy Blue%' AND product_name LIKE '%Trousers%';

UPDATE Products SET image_url = 'images/image2/Men/Bottoms/rippedjeans_Vans.jpg'
WHERE product_name LIKE '%Ripped%' AND product_name LIKE '%Jeans%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Men%');

UPDATE Products SET image_url = 'images/image2/Men/Bottoms/WhiteChinos.png'
WHERE product_name LIKE '%White%' AND product_name LIKE '%Chinos%';

-- Men's Hoodies
UPDATE Products SET image_url = 'images/image2/Men/Hoodies/ClassicBlack_ZARA.png'
WHERE product_name LIKE '%ZARA%' AND product_name LIKE '%Black%' AND product_name LIKE '%Hoodie%';

UPDATE Products SET image_url = 'images/image2/Men/Hoodies/CremeColor.png'
WHERE product_name LIKE '%Creme%' AND product_name LIKE '%Hoodie%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Men%');

UPDATE Products SET image_url = 'images/image2/Men/Hoodies/LightOrange_By_OFFWHITE.png'
WHERE product_name LIKE '%Off White%' AND product_name LIKE '%Orange%' AND product_name LIKE '%Hoodie%';

UPDATE Products SET image_url = 'images/image2/Men/Hoodies/VansOriginalBlack.png'
WHERE product_name LIKE '%Vans%' AND product_name LIKE '%Black%' AND product_name LIKE '%Hoodie%';

UPDATE Products SET image_url = 'images/image2/Men/Hoodies/WhiteSUEDE.png'
WHERE product_name LIKE '%Suede%' AND product_name LIKE '%White%' AND product_name LIKE '%Hoodie%';

-- Men's Jackets
UPDATE Products SET image_url = 'images/image2/Men/Jackets/BlackLeatherJacket.png'
WHERE product_name LIKE '%Black%' AND product_name LIKE '%Leather%' AND product_name LIKE '%Jacket%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Men%');

UPDATE Products SET image_url = 'images/image2/Men/Jackets/Lightgrey.jpg'
WHERE product_name LIKE '%Light Grey%' AND product_name LIKE '%Jacket%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Men%');

UPDATE Products SET image_url = 'images/image2/Men/Jackets/NavyBlueJ.jpg'
WHERE product_name LIKE '%Navy Blue%' AND product_name LIKE '%Jacket%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Men%');

UPDATE Products SET image_url = 'images/image2/Men/Jackets/OliveGreen.png'
WHERE product_name LIKE '%Olive Green%' AND product_name LIKE '%Jacket%';

UPDATE Products SET image_url = 'images/image2/Men/Jackets/Turquoise.jpg'
WHERE product_name LIKE '%Turquoise%' AND product_name LIKE '%Jacket%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Men%');

-- Men's Shoes
UPDATE Products SET image_url = 'images/image2/Men/Shoes/BrownLEATHERFormals.jpg'
WHERE product_name LIKE '%Brown%' AND product_name LIKE '%Leather%' AND product_name LIKE '%Formal%';

UPDATE Products SET image_url = 'images/image2/Men/Shoes/NikeAirForce1.png'
WHERE product_name LIKE '%Nike%' AND product_name LIKE '%Air Force%';

UPDATE Products SET image_url = 'images/image2/Men/Shoes/NikeAirMaxProductRed.jpg'
WHERE product_name LIKE '%Nike%' AND product_name LIKE '%Air Max%';

UPDATE Products SET image_url = 'images/image2/Men/Shoes/NikeColorBlock.png'
WHERE product_name LIKE '%Nike%' AND product_name LIKE '%Color Block%';

UPDATE Products SET image_url = 'images/image2/Men/Shoes/UnisexBlackConverse.jpg'
WHERE product_name LIKE '%Converse%' AND product_name LIKE '%Black%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Men%');

UPDATE Products SET image_url = 'images/image2/Men/Shoes/Vans.png'
WHERE product_name LIKE '%Vans%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Men%');

UPDATE Products SET image_url = 'images/image2/Men/Shoes/YeezyAdidas.png'
WHERE product_name LIKE '%Yeezy%' OR product_name LIKE '%Adidas Yeezy%';

-- Men's T-Shirts
UPDATE Products SET image_url = 'images/image2/Men/Tees/AwesomeSolidWhiteColor.jpg'
WHERE product_name LIKE '%White%' AND product_name LIKE '%T-Shirt%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Men%');

UPDATE Products SET image_url = 'images/image2/Men/Tees/BlackGraphicTee.png'
WHERE product_name LIKE '%Black%' AND product_name LIKE '%Graphic%' AND product_name LIKE '%T-Shirt%';

UPDATE Products SET image_url = 'images/image2/Men/Tees/BlackScripted.png'
WHERE product_name LIKE '%Black%' AND product_name LIKE '%Script%' AND product_name LIKE '%T-Shirt%';

UPDATE Products SET image_url = 'images/image2/Men/Tees/EnscriptionbyChampion.png'
WHERE product_name LIKE '%Champion%' AND product_name LIKE '%T-Shirt%';

UPDATE Products SET image_url = 'images/image2/Men/Tees/SolidFlameOrange.png'
WHERE product_name LIKE '%Orange%' AND product_name LIKE '%T-Shirt%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Men%');

-- =====================================================
-- WOMEN'S CATEGORY
-- =====================================================

-- Women's Bags
UPDATE Products SET image_url = 'images/Women/BlackBag1.png'
WHERE product_name LIKE '%Black%' AND product_name LIKE '%Bag%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/gabrielle_henderson_ShoulderBag.png'
WHERE product_name LIKE '%Shoulder Bag%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/OrangeBag2.png'
WHERE product_name LIKE '%Orange%' AND product_name LIKE '%Bag%';

UPDATE Products SET image_url = 'images/Women/OWL_colorblocked_ALDO.png'
WHERE product_name LIKE '%Aldo%' OR (product_name LIKE '%Colorblock%' AND product_name LIKE '%Bag%');

UPDATE Products SET image_url = 'images/Women/SolidWhiteLouisVuitton.png'
WHERE product_name LIKE '%Louis Vuitton%' OR (product_name LIKE '%White%' AND product_name LIKE '%Designer%');

-- Women's Bottoms
UPDATE Products SET image_url = 'images/Women/bottom/ArmyCargoPants.png'
WHERE product_name LIKE '%Army%' AND product_name LIKE '%Cargo%';

UPDATE Products SET image_url = 'images/Women/bottom/BlackJoggers.png'
WHERE product_name LIKE '%Black%' AND product_name LIKE '%Joggers%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/bottom/Bluedenim.png'
WHERE product_name LIKE '%Blue%' AND product_name LIKE '%Denim%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/bottom/FloralBottoms.png'
WHERE product_name LIKE '%Floral%' AND (product_name LIKE '%Pants%' OR product_name LIKE '%Bottom%');

UPDATE Products SET image_url = 'images/Women/bottom/Palazo_grey.png'
WHERE product_name LIKE '%Grey%' AND product_name LIKE '%Palazzo%';

UPDATE Products SET image_url = 'images/Women/bottom/RippedJeans_MARKNSPENCER.png'
WHERE product_name LIKE '%Ripped%' AND product_name LIKE '%Jeans%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/bottom/WhitePalazo.png'
WHERE product_name LIKE '%White%' AND product_name LIKE '%Palazzo%';

-- Women's Hoodies
UPDATE Products SET image_url = 'images/Women/Hoodies/AdidasColorBlock.jpg'
WHERE product_name LIKE '%Adidas%' AND product_name LIKE '%ColorBlock%' AND product_name LIKE '%Hoodie%';

UPDATE Products SET image_url = 'images/Women/Hoodies/Beige.png'
WHERE product_name LIKE '%Beige%' AND product_name LIKE '%Hoodie%';

UPDATE Products SET image_url = 'images/Women/Hoodies/grey.png'
WHERE product_name LIKE '%Grey%' AND product_name LIKE '%Hoodie%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/Hoodies/SolidPurpleHaze.png'
WHERE product_name LIKE '%Purple%' AND product_name LIKE '%Hoodie%';

UPDATE Products SET image_url = 'images/Women/Hoodies/Violet.png'
WHERE product_name LIKE '%Violet%' AND product_name LIKE '%Hoodie%';

-- Women's Jackets
UPDATE Products SET image_url = 'images/Women/jackets/ArmyGreenCargo.png'
WHERE product_name LIKE '%Army Green%' AND product_name LIKE '%Jacket%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/jackets/BlackDoubleButton.png'
WHERE product_name LIKE '%Black%' AND product_name LIKE '%Double Button%' AND product_name LIKE '%Jacket%';

UPDATE Products SET image_url = 'images/Women/jackets/Checked_Jacket.png'
WHERE product_name LIKE '%Checked%' AND product_name LIKE '%Jacket%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/jackets/DarkBlue.png'
WHERE product_name LIKE '%Dark Blue%' AND product_name LIKE '%Jacket%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/jackets/DarkBrownLeather.png'
WHERE product_name LIKE '%Dark Brown%' AND product_name LIKE '%Leather%' AND product_name LIKE '%Jacket%';

UPDATE Products SET image_url = 'images/Women/jackets/RedLeather.png'
WHERE product_name LIKE '%Red%' AND product_name LIKE '%Leather%' AND product_name LIKE '%Jacket%';

UPDATE Products SET image_url = 'images/Women/jackets/turquoise.png'
WHERE product_name LIKE '%Turquoise%' AND product_name LIKE '%Jacket%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/jackets/WhitePuffed.png'
WHERE product_name LIKE '%White%' AND product_name LIKE '%Puffed%' AND product_name LIKE '%Jacket%';

-- Women's Shoes
UPDATE Products SET image_url = 'images/Women/Shoes/ClassicBlackConverse.jpg'
WHERE product_name LIKE '%Converse%' AND product_name LIKE '%Black%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/Shoes/FloralHeels.png'
WHERE product_name LIKE '%Floral%' AND product_name LIKE '%Heels%';

UPDATE Products SET image_url = 'images/Women/Shoes/LightPink_SaintLauren.png'
WHERE product_name LIKE '%Pink%' AND product_name LIKE '%Saint Laurent%';

UPDATE Products SET image_url = 'images/Women/Shoes/NikeColorBlock.png'
WHERE product_name LIKE '%Nike%' AND product_name LIKE '%Color Block%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/Shoes/Peace_BlackChanel.jpg'
WHERE product_name LIKE '%Chanel%' OR (product_name LIKE '%Black%' AND product_name LIKE '%Designer%' AND product_name LIKE '%Shoe%');

-- Women's T-Shirts
UPDATE Products SET image_url = 'images/Women/Tees/OrangeTee.jpg'
WHERE product_name LIKE '%Adidas%' AND product_name LIKE '%T-Shirt%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/Tees/DarkGrey.jpg'
WHERE product_name LIKE '%Dark Grey%' AND product_name LIKE '%T-Shirt%';

UPDATE Products SET image_url = 'images/Women/Tees/FloralGucciLogo.jpg'
WHERE product_name LIKE '%Gucci%' OR (product_name LIKE '%Floral%' AND product_name LIKE '%T-Shirt%');

UPDATE Products SET image_url = 'images/Women/Tees/solidBlacktee.jpg'
WHERE product_name LIKE '%Black%' AND product_name LIKE '%T-Shirt%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/Tees/VogueWhiteTEE.jpg'
WHERE product_name LIKE '%Vogue%' OR (product_name LIKE '%White%' AND product_name LIKE '%T-Shirt%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%'));

-- Women's Watches
UPDATE Products SET image_url = 'images/Women/Rolex.png'
WHERE product_name LIKE '%Rolex%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/RoseGold.png'
WHERE product_name LIKE '%Rose Gold%' AND product_name LIKE '%Watch%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Women%');

UPDATE Products SET image_url = 'images/Women/Metallic.png'
WHERE product_name LIKE '%Metallic%' AND product_name LIKE '%Watch%';

-- =====================================================
-- KIDS CATEGORY
-- =====================================================

-- Kids Shoes
UPDATE Products SET image_url = 'images/Kids/BrownGirlBoots.jpg'
WHERE product_name LIKE '%Brown%' AND product_name LIKE '%Boots%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Kids%');

UPDATE Products SET image_url = 'images/Kids/Converse.jpg'
WHERE product_name LIKE '%Converse%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Kids%');

UPDATE Products SET image_url = 'images/Kids/Silvershoes.jpg'
WHERE product_name LIKE '%Silver%' AND product_name LIKE '%Shoes%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Kids%');

UPDATE Products SET image_url = 'images/Kids/WhiteGirl.jpg'
WHERE product_name LIKE '%White%' AND product_name LIKE '%Girl%' AND product_name LIKE '%Shoes%';

-- Kids Hoodies
UPDATE Products SET image_url = 'images/Kids/Hoodie/CremeBrown.jpg'
WHERE product_name LIKE '%Creme%' OR product_name LIKE '%Brown%' AND product_name LIKE '%Hoodie%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Kids%');

UPDATE Products SET image_url = 'images/Kids/Hoodie/GreyKnit.jpg'
WHERE product_name LIKE '%Grey%' AND product_name LIKE '%Knit%' AND product_name LIKE '%Hoodie%';

UPDATE Products SET image_url = 'images/Kids/Hoodie/SkyBlue.jpg'
WHERE product_name LIKE '%Sky Blue%' AND product_name LIKE '%Hoodie%';

UPDATE Products SET image_url = 'images/Kids/Hoodie/WhiteFur.jpg'
WHERE product_name LIKE '%White%' AND product_name LIKE '%Fur%' AND product_name LIKE '%Hoodie%';

-- Kids Jackets
UPDATE Products SET image_url = 'images/Kids/Jacket/BoyesBlueDenim.jpg'
WHERE product_name LIKE '%Boys%' AND product_name LIKE '%Blue%' AND product_name LIKE '%Denim%';

UPDATE Products SET image_url = 'images/Kids/Jacket/BoysBlackDenim.jpg'
WHERE product_name LIKE '%Boys%' AND product_name LIKE '%Black%' AND product_name LIKE '%Denim%';

UPDATE Products SET image_url = 'images/Kids/Jacket/Girls_Denim.jpg'
WHERE product_name LIKE '%Girls%' AND product_name LIKE '%Denim%' AND product_name LIKE '%Jacket%';

UPDATE Products SET image_url = 'images/Kids/Jacket/GirlsOvercoat.jpg'
WHERE product_name LIKE '%Girls%' AND product_name LIKE '%Overcoat%';

UPDATE Products SET image_url = 'images/Kids/Jacket/WoolenJacket.jpg'
WHERE product_name LIKE '%Woolen%' AND product_name LIKE '%Jacket%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Kids%');

-- Kids Jeans
UPDATE Products SET image_url = 'images/Kids/Jeans/CargoDenims.jpg'
WHERE product_name LIKE '%Cargo%' AND product_name LIKE '%Denim%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Kids%');

UPDATE Products SET image_url = 'images/Kids/Jeans/DarkBlueDenims.jpg'
WHERE product_name LIKE '%Dark Blue%' AND product_name LIKE '%Denim%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Kids%');

UPDATE Products SET image_url = 'images/Kids/Jeans/GirlsBascketballJeans.jpg'
WHERE product_name LIKE '%Girls%' AND product_name LIKE '%Basketball%' AND product_name LIKE '%Jeans%';

UPDATE Products SET image_url = 'images/Kids/Jeans/GirlsRippedJeans.jpg'
WHERE product_name LIKE '%Girls%' AND product_name LIKE '%Ripped%' AND product_name LIKE '%Jeans%';

-- Kids T-Shirts
UPDATE Products SET image_url = 'images/Kids/Tees/ColorBlock.jpg'
WHERE product_name LIKE '%Color Block%' AND product_name LIKE '%T-Shirt%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Kids%');

UPDATE Products SET image_url = 'images/Kids/Tees/GirlsAbstract.jpg'
WHERE product_name LIKE '%Girls%' AND product_name LIKE '%Abstract%' AND product_name LIKE '%T-Shirt%';

UPDATE Products SET image_url = 'images/Kids/Tees/WhiteSolid.png'
WHERE product_name LIKE '%White%' AND product_name LIKE '%Solid%' AND product_name LIKE '%T-Shirt%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Kids%');

-- Kids Watches
UPDATE Products SET image_url = 'images/Kids/Watch/Bluewatch.jpg'
WHERE product_name LIKE '%Blue%' AND product_name LIKE '%Watch%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Kids%');

UPDATE Products SET image_url = 'images/Kids/Watch/Girlswatchpink.jpg'
WHERE product_name LIKE '%Girls%' AND product_name LIKE '%Pink%' AND product_name LIKE '%Watch%';

UPDATE Products SET image_url = 'images/Kids/Watch/Orange.jpg'
WHERE product_name LIKE '%Orange%' AND product_name LIKE '%Watch%' AND category_id = (SELECT category_id FROM Categories WHERE category_name LIKE '%Kids%');

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================

-- Check how many products now have images
SELECT
    COUNT(*) as total_products,
    SUM(CASE WHEN image_url IS NOT NULL AND image_url != '' THEN 1 ELSE 0 END) as products_with_images,
    SUM(CASE WHEN image_url IS NULL OR image_url = '' THEN 1 ELSE 0 END) as products_without_images
FROM Products;

-- Show products by category with image status
SELECT
    c.category_name,
    COUNT(p.product_id) as total_products,
    SUM(CASE WHEN p.image_url IS NOT NULL AND p.image_url != '' THEN 1 ELSE 0 END) as with_images,
    SUM(CASE WHEN p.image_url IS NULL OR p.image_url = '' THEN 1 ELSE 0 END) as without_images
FROM Products p
JOIN Categories c ON p.category_id = c.category_id
GROUP BY c.category_name
ORDER BY c.category_name;

-- List products without images
SELECT
    p.product_id,
    p.product_name,
    c.category_name
FROM Products p
JOIN Categories c ON p.category_id = c.category_id
WHERE p.image_url IS NULL OR p.image_url = ''
ORDER BY c.category_name, p.product_name;

-- =====================================================
-- COMPLETE!
-- =====================================================

