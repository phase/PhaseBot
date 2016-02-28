#Script to build Rust lib and move it to res/lib/
cargo build
mv target/debug/phasebot.dll ./