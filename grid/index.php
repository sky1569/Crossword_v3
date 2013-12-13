<?php
$content = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
$content .= "<gridlist>";
foreach (glob("*.xml") as $filename)
  $content .= "<grid>$filename</grid>";
$content .= "</gridlist>";

header("content-type: text/xml");
header("Content-Length: ".strlen($content)); 
header("Content-Disposition: attachment; filename=\"gridlist.xml\"");
echo $content;

?>