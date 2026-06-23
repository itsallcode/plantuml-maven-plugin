File target = new File(basedir, "target/generated-diagrams");

// [itest->dsn~render-png~1]
// [itest->dsn~render-svg~1]
// [itest->dsn~render-default-output-directory~1]
// [itest->dsn~configure-output-directory~1]
assert target.exists() && target.isDirectory()

File pngFile = new File(target, "test.png");
assert pngFile.exists()

File svgFile = new File(target, "test.svg");
assert svgFile.exists()

return true;
