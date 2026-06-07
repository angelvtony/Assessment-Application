# Workspace3D - Android 3D Model Viewer

A high-performance, single-activity Android application built with Jetpack Compose that allows users to seamlessly load, view, and interact with multiple 3D `.glb` models concurrently.

## Architecture & Technology Stack
- **UI Framework:** Jetpack Compose (MVI Architecture / Clean Architecture)
- **Dependency Injection:** Dagger Hilt
- **3D Rendering Library:** SceneView (based on Google Filament)

## 1. 3D Library Used & Why
I chose **SceneView** (which wraps Google's Filament engine) over alternatives like raw OpenGL, Unity (as a library), or Webview-based solutions (like `<model-viewer>`). 
- **Native Performance:** It leverages Filament's highly optimized physically based rendering (PBR) pipeline for Android, achieving near-photorealistic lighting and native frame rates.
- **Compose Integration:** SceneView provides excellent idiomatic wrappers for Jetpack Compose, allowing us to embed 3D scenes directly within declarative UI components without wrestling with traditional `SurfaceView` lifecycle issues.
- **Efficient `.glb` parsing:** It natively parses and renders Khronos glTF/glb files efficiently in memory.

## 2. Performance Optimizations
- **Shared Rendering Context:** Instead of initializing a new rendering `Engine` and `ModelLoader` for every model added to the screen, a single engine and loader are instantiated at the root of the screen and passed down. This drastically reduces memory overhead and prevents Out-Of-Memory (OOM) crashes when 5+ models are loaded.
- **MVI State Management:** State is centralized in a Reducer. Only the specific coordinates (X, Y) and scale of the dragged/resized models are updated. We use derived state and immutable data classes to ensure Jetpack Compose only recomposes the specific `ModelCard` being interacted with, avoiding full-screen recompositions.
- **Achromatic Precision Design:** By utilizing a minimalist, high-contrast dark theme, we minimize overdraw and avoid rendering complex background gradients, leaving maximum GPU resources available for the 3D Filament renderer.
- **Deferred Lighting:** Heavy lighting nodes (Key, Fill, Back lights) are conditionally managed and tied strictly to the lifecycle of their respective model containers.

## 3. Trade-Offs Made
- **Isolated Scenes vs. Single Global Scene:** To satisfy the requirement of having "draggable containers" with UI borders, each model is rendered in its own isolated `Scene` inside a Compose `Box`. A more performant approach for rendering 10+ models would be to have a *single* fullscreen `SceneView` and manage models as spatial nodes within that 3D space. I traded maximum 3D rendering performance for better adherence to standard 2D UI dragging/window-management requirements.
- **Gestures:** Because each model is wrapped in an interactive 2D Compose container, standard 2D Pan/Zoom gestures intercept the touches. I had to explicitly separate "Window Move/Resize" from "3D Rotate/Zoom" using a toggle button to prevent gesture conflicts.

## 4. Future Improvements
- **Unified 3D World Space:** Transition from multiple `Scene` composables to a single global `Scene`. Window borders and drag handles could be projected into 2D UI space over the 3D canvas, vastly improving battery life and rendering overhead.
- **Asynchronous Model Streaming:** Currently, models are bundled in `assets/`. With more time, I would implement a remote repository layer using Kotlin Coroutines and Retrofit to stream chunked `.glb` files from a server and cache them locally using `Room`.
- **Lighting Harmonization:** Implement Image-Based Lighting (IBL) using an HDRI environment map so that all models share realistic, uniform environmental reflections.

## 5. Known Bugs & Limitations
- **Heavy Memory Footprint:** Since each model initiates its own `Scene`, devices with low RAM (under 4GB) might experience frame drops or lifecycle terminations if the user spawns more than 8-10 highly complex models simultaneously.
- **Z-Index Overlap:** Because the containers are standard 2D Compose elements, models cannot physically intersect or cast shadows on one another. A model is strictly confined to the clipping bounds of its container card.
