export interface ImageCompressionOptions {
  maxDimension?: number
  maxBytes?: number
  quality?: number
}

const dataUrlBytes = (dataUrl: string) => Math.ceil((dataUrl.length - dataUrl.indexOf(',') - 1) * 0.75)

async function loadImage(file: File): Promise<{ source: CanvasImageSource; width: number; height: number; close: () => void }> {
  if ('createImageBitmap' in window) {
    const bitmap = await createImageBitmap(file, { imageOrientation: 'from-image' })
    return { source: bitmap, width: bitmap.width, height: bitmap.height, close: () => bitmap.close() }
  }
  const url = URL.createObjectURL(file)
  const image = new Image()
  image.src = url
  await image.decode()
  return { source: image, width: image.naturalWidth, height: image.naturalHeight, close: () => URL.revokeObjectURL(url) }
}

export async function compressImage(
  file: File,
  options: ImageCompressionOptions = {},
): Promise<string> {
  if (!file.type.startsWith('image/')) throw new Error('请选择图片文件')
  if (file.size > 20 * 1024 * 1024) throw new Error('原始图片不能超过 20MB')

  const maxDimension = options.maxDimension ?? 1600
  const maxBytes = options.maxBytes ?? 500 * 1024
  let quality = options.quality ?? 0.84
  const image = await loadImage(file)
  const ratio = Math.min(1, maxDimension / Math.max(image.width, image.height))
  let width = Math.max(1, Math.round(image.width * ratio))
  let height = Math.max(1, Math.round(image.height * ratio))
  const canvas = document.createElement('canvas')
  const context = canvas.getContext('2d')
  if (!context) {
    image.close()
    throw new Error('当前浏览器不支持图片压缩')
  }

  const render = () => {
    canvas.width = width
    canvas.height = height
    context.fillStyle = '#fff'
    context.fillRect(0, 0, width, height)
    context.drawImage(image.source, 0, 0, width, height)
    return canvas.toDataURL('image/jpeg', quality)
  }

  let result = render()
  while (dataUrlBytes(result) > maxBytes && quality > 0.58) {
    quality -= 0.08
    result = render()
  }
  while (dataUrlBytes(result) > maxBytes && Math.max(width, height) > 900) {
    width = Math.round(width * 0.85)
    height = Math.round(height * 0.85)
    result = render()
  }
  image.close()
  return result
}
