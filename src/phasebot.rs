#![crate_type = "dylib"]

#[macro_use]
extern crate lazy_static;

use std::sync::Mutex;
use std::collections::HashSet;

#[repr(C)]
pub struct Vec3d {
    x: f64,
    y: f64,
    z: f64,
}

#[derive(Eq, PartialEq, Hash)]
struct Chunk {
    x: i32,
    y: i32,
    z: i32,
    blocks: [[[i32; 16]; 16]; 16],
}

lazy_static! {
    static ref CHUNKS: Mutex<HashSet<Chunk>> = Mutex::new(HashSet::new());
}

pub extern fn addChunk(cx: i32, cy: i32, cz: i32, c_blocks: [[[i32; 16]; 16]; 16]) {
    CHUNKS.lock().unwrap().insert(Chunk {x: cx, y: cy, z: cz, blocks: c_blocks});
}